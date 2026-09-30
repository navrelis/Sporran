package dev.sporran

import com.google.gson.GsonBuilder
import com.mojang.datafixers.util.Either
import io.github.fabricators_of_create.porting_lib.entity.events.EntityEvents
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingDropsEvent
import io.github.fabricators_of_create.porting_lib.entity.events.player.CriticalHitEvent
import io.github.fabricators_of_create.porting_lib.entity.events.player.PlayerInteractEvent
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents
import net.fabricmc.fabric.api.util.TriState
import net.minecraft.core.BlockPos
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.util.Unit
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.bus.api.EventPriority
import net.neoforged.neoforge.common.CommonHooks
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.common.extensions.IBlockExtension
import net.neoforged.neoforge.event.EventHooks
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent
import net.neoforged.neoforge.event.level.LevelEvent
import net.neoforged.neoforge.server.ServerLifecycleHooks
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import dev.sporran.client.SporranClient
import dev.sporran.helpers.ArchitecturyLivingDeathBridge
import dev.sporran.injections.world.entity.LivingEntityInjection
import dev.sporran.loader.SporranLoader
import dev.sporran.mixin.MinecraftServerAccessor
import dev.sporran.util.SporranHelper

class Sporran : ModInitializer {
    override fun onInitialize() {
        // We have no reason to retain this info.
        SporranHelper.clearForgeClassNodes()

        registerFabricEvents()
    }

    @Suppress("removal")
    private fun registerFabricEvents() {
        // Sporran: We rely on this event so
        /*PlayerInteractEvent.RightClickBlock.EVENT.register { event ->
            val forgeEvent = CommonHooks.onRightClickBlock(event.entity, event.hand, event.pos, event.hitVec)

            event.cancellationResult = forgeEvent.cancellationResult

            if (!forgeEvent.useBlock.isDefault)
                event.useBlock = TriState.of(forgeEvent.useBlock.isTrue)

            if (!forgeEvent.useItem.isDefault)
                event.useItem = TriState.of(forgeEvent.useItem.isTrue)
        }*/

        // Sporran: CommonHooks returns the cancellation result only when the NeoForge event was cancelled. Porting Lib only
        //  uses the result of a cancelled event, so the event has to be cancelled as well (null would also wipe the
        //  result of a Porting Lib listener).
        PlayerInteractEvent.RightClickItem.EVENT.register { event ->
            // Sporran: on the client MultiPlayerGameModeInject already fires NeoForge's RightClickItem at the same point.
            if (event.entity.level().isClientSide)
                return@register

            val cancelResult = CommonHooks.onItemRightClick(event.entity, event.hand)
            if (cancelResult != null) {
                event.cancellationResult = cancelResult
                event.isCanceled = true
            }
        }

        PlayerInteractEvent.EntityInteract.EVENT.register { event ->
            val cancelResult = CommonHooks.onInteractEntity(event.entity, event.target, event.hand)
            if (cancelResult != null) {
                event.cancellationResult = cancelResult
                event.isCanceled = true
            }
        }

        // Sporran: the only source of NeoForge's EntityInteractSpecific on both sides; Porting Lib fires it at the same
        //  points as NeoForge (before Entity.interactAt).
        PlayerInteractEvent.EntityInteractSpecific.EVENT.register { event ->
            val cancelResult = CommonHooks.onInteractEntityAt(event.entity, event.target, event.localPos, event.hand)
            if (cancelResult != null) {
                event.cancellationResult = cancelResult
                event.isCanceled = true
            }
        }

        PlayerInteractEvent.LeftClickBlock.EVENT.register { event ->
            // Sporran: on the client MultiPlayerGameModeInject already fires NeoForge's LeftClickBlock (and CLIENT_HOLD
            //  through onClientMineHold) at the same points, only the server side comes from Porting Lib.
            if (event.entity.level().isClientSide)
                return@register

            val forgeEvent = CommonHooks.onLeftClickBlock(event.entity, event.pos, event.face, when (event.action) {
                PlayerInteractEvent.LeftClickBlock.Action.START -> ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK
                PlayerInteractEvent.LeftClickBlock.Action.STOP -> ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK
                PlayerInteractEvent.LeftClickBlock.Action.ABORT -> ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK
                else -> ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK
            })

            // Sporran: Porting Lib reads useBlock (BlockState.attack) from its own event
            if (!forgeEvent.useBlock.isDefault)
                event.useBlock = TriState.of(forgeEvent.useBlock.isTrue)

            if (!forgeEvent.useItem.isDefault)
                event.useItem = TriState.of(forgeEvent.useItem.isTrue)

            if (forgeEvent.isCanceled)
                event.isCanceled = true
        }

        // Sporran: NeoForge fires CriticalHitEvent once in Player.attack, with the attacked entity as target, and uses
        //  isCriticalHit and the damage multiplier of the result. Porting Lib fires its CriticalHitEvent at that point.
        CriticalHitEvent.EVENT.register { event ->
            val forgeEvent = net.neoforged.neoforge.event.entity.player.CriticalHitEvent(event.entity, event.target, event.vanillaMultiplier, event.isVanillaCritical)
            forgeEvent.isCriticalHit = event.isCriticalHit
            forgeEvent.damageMultiplier = event.damageMultiplier
            NeoForge.EVENT_BUS.post(forgeEvent)

            event.isCriticalHit = forgeEvent.isCriticalHit
            event.damageMultiplier = forgeEvent.damageMultiplier
        }

        PlayerInteractEvent.LeftClickEmpty.EVENT.register { event ->
            CommonHooks.onEmptyLeftClick(event.entity)
        }

        // Sporran: NeoForge's RightClickEmpty is fired by MinecraftInject#rightClickAir, the same point where Porting Lib
        //  fires its RightClickEmpty. Bridging it as well fired it twice per hand.

        EntitySleepEvents.ALLOW_SLEEPING.register { player, pos ->
            if (player !is ServerPlayer) // istg
                return@register null

            EventHooks.canPlayerStartSleeping(player, pos, Either.right(Unit.INSTANCE)).left().orElse(null)
        }

        EntitySleepEvents.SET_BED_OCCUPATION_STATE.register { entity, pos, state, occupied ->
            if (
                SporranHelper.hasMethodOverride(
                    state.block.javaClass, IBlockExtension::class.java, "setBedOccupied",
                    BlockState::class.java, Level::class.java, BlockPos::class.java,
                    LivingEntity::class.java, Boolean::class.javaPrimitiveType!!
                )
            ) {
                state.setBedOccupied(entity.level(), pos, entity, occupied)
                return@register true
            } else {
                return@register false
            }
        }

        EntitySleepEvents.ALLOW_BED.register { entity, pos, state, bool ->
            if (
                SporranHelper.hasMethodOverride(
                    state.block.javaClass, IBlockExtension::class.java, "isBed",
                    BlockState::class.java, BlockGetter::class.java, BlockPos::class.java, LivingEntity::class.java
                )
            ) {
                return@register if (state.isBed(entity.level(), pos, entity)) {
                    InteractionResult.SUCCESS
                } else {
                    InteractionResult.FAIL
                }
            }
            return@register InteractionResult.PASS
        }

        EntitySleepEvents.MODIFY_SLEEPING_DIRECTION.register { entity, pos, direction ->
            val state: BlockState = entity.level().getBlockState(pos)
            if (
                (
                    SporranHelper.hasMethodOverride(
                        state.block.javaClass, IBlockExtension::class.java, "getBedDirection",
                        BlockState::class.java, LevelReader::class.java, BlockPos::class.java
                    ) || // If bed is not BedBlock we need to run the NeoForge getBedDirection for the correct result even if not overridden.
                    SporranHelper.hasMethodOverride(
                        state.block.javaClass, IBlockExtension::class.java, "isBed",
                        BlockState::class.java, BlockGetter::class.java, BlockPos::class.java, LivingEntity::class.java
                    )
                ) &&
                state.isBed(entity.level(), pos, entity)
            ) {
                return@register state.getBedDirection(entity.level(), pos)
            }
            return@register direction
        }

        EntitySleepEvents.ALLOW_SETTING_SPAWN.register { player, pos ->
            !EventHooks.onPlayerSpawnSet(player, player.level().dimension(), pos, false)
        }

        ServerLifecycleEvents.SERVER_STARTED.register {
            ServerLifecycleHooks.handleServerStarted(it)
        }

        ServerLifecycleEvents.SERVER_STOPPING.register {
            ServerLifecycleHooks.handleServerStopping(it)
        }

        ServerLifecycleEvents.SERVER_STOPPED.register {
            ServerLifecycleHooks.expectServerStopped()
            ServerLifecycleHooks.handleServerStopped(it)
        }

        // Sporran: ExplosionEvent.Start and .Detonate are fired by LevelInject and ExplosionInject, at the same points
        //  as Porting Lib's ExplosionEvents.START / DETONATE. Bridging those as well fired both events twice.

        EntityEvents.EnteringSection.EVENT.register { event ->
            CommonHooks.onEntityEnterSection(event.entity, event.packedOldPos, event.packedNewPos)
        }

        ServerTickEvents.START_SERVER_TICK.register { server ->
            EventHooks.fireServerTickPre((server as MinecraftServerAccessor)::callHaveTime, server)
        }

        ServerTickEvents.END_SERVER_TICK.register { server ->
            EventHooks.fireServerTickPost((server as MinecraftServerAccessor)::callHaveTime, server)
        }

        ServerTickEvents.START_WORLD_TICK.register { level ->
            EventHooks.fireLevelTickPre(level, (level.server as MinecraftServerAccessor)::callHaveTime)
        }

        ServerTickEvents.END_WORLD_TICK.register { level ->
            EventHooks.fireLevelTickPost(level, (level.server as MinecraftServerAccessor)::callHaveTime)
        }

        // Sporran: NeoForge's PlayerTickEvent.Pre/Post are fired by PlayerInject (Player.tick HEAD/TAIL), like NeoForge's own
        // patch. Bridging Porting Lib's PlayerTickEvent here as well fired both events twice per tick, so every
        // NeoForge player tick handler ran twice (e.g. Cataclysm's ChargeAttachment timer counted down at double speed).

        ServerWorldEvents.LOAD.register { server, level ->
            NeoForge.EVENT_BUS.post(LevelEvent.Load(level))
        }

        ServerWorldEvents.UNLOAD.register { server, level ->
            NeoForge.EVENT_BUS.post(LevelEvent.Unload(level))
        }

        LivingDropsEvent.EVENT.register { event ->
            if (CommonHooks.onLivingDrops(event.entity, event.source, event.drops, event.isRecentlyHit))
                event.isCanceled = true
        }

        // Sporran: track LivingDeathEvent and Fabric's AFTER_DEATH per entity, so deaths of NeoForge mod entities
        //  that override die() without calling super still reach Fabric (see LivingEntityInject#sporran$reportModdedDeathToFabric).
        //  Only the event object is stored, its cancellation state is read once the whole die() call is done.
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, true, LivingDeathEvent::class.java) { event ->
            (event.entity as LivingEntityInjection).`sporran$setPendingDeathEvent`(event)
        }

        ServerLivingEntityEvents.AFTER_DEATH.register { entity, _ ->
            (entity as LivingEntityInjection).`sporran$markFabricDeathReported`()
        }

        // Sporran: the same gap for Architectury's EntityEvent.LIVING_DEATH (FTB Quests kill tasks), only if Architectury is loaded.
        ArchitecturyLivingDeathBridge.register()

        EntityElytraEvents.CUSTOM.register { entity, tickElytra ->
            val chestPiece = entity.getItemBySlot(EquipmentSlot.CHEST)
            chestPiece.canElytraFly(entity) && chestPiece.elytraFlightTick(entity, entity.fallFlyingTicks)
        }
    }

    companion object {
        /**
         * The Fabric mod ID of this jar (fabric.mod.json "id"). Also used as the Knit loader ID, as the namespace
         * for registry/tag/layer IDs and as the MixinExtras Share namespace.
         */
        const val MOD_ID = "sporran"

        lateinit var instance: Sporran
        val logger: Logger = LoggerFactory.getLogger(Sporran::class.java)
        val loader: SporranLoader
            get() = SporranLoader.instance
        val gson = GsonBuilder().setPrettyPrinting().create()

        fun load(onServer: Boolean) {
            // config load should be here
            var loaded = false

            if (!onServer) {
                SporranClient.lateRegisterEvents()
            }
        }
    }
}
