package dev.sporran.compat.transfer

import io.github.fabricators_of_create.porting_lib.transfer.MutableContainerItemContext
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.capabilities.*
import team.reborn.energy.api.EnergyStorage
import dev.sporran.compat.transfer.capability.FabricLookupAsBlockCapabilityProvider
import dev.sporran.compat.transfer.capability.FabricLookupAsItemCapabilityProvider
import dev.sporran.compat.transfer.capability.FabricLookupAsSlottedBlockCapabilityProvider
import dev.sporran.compat.transfer.capability.FabricLookupAsSlottedItemCapabilityProvider
import dev.sporran.compat.transfer.energy.FabricEnergyStorageCapability
import dev.sporran.compat.transfer.energy.NeoForgeEnergyStorage
import dev.sporran.compat.transfer.fluid.FabricFluidItemStorageCapability
import dev.sporran.compat.transfer.fluid.FabricFluidStorageCapability
import dev.sporran.compat.transfer.fluid.NeoForgeFluidStorage
import dev.sporran.compat.transfer.item.FabricItemStorageCapability
import dev.sporran.compat.transfer.item.NeoForgeSlottedStorage
import dev.sporran.compat.transfer.mixin.BlockCapabilityAccessor
import dev.sporran.compat.transfer.mixin.EntityCapabilityAccessor
import dev.sporran.compat.transfer.mixin.ItemCapabilityAccessor

class TransferInterop : ModInitializer {
    override fun onInitialize() {
//        NeoForge.EVENT_BUS.register(this)

        // NeoForge Capabilities inside Fabric Lookups
        ItemStorage.SIDED.registerFallback { world, pos, state, blockEntity, direction ->
            if (blockEntity == null)
                return@registerFallback null

            val handler = Capabilities.ItemHandler.BLOCK.getCapability(world, pos, state, blockEntity, direction)

            if (handler != null && handler !is FabricItemStorageCapability) {
                return@registerFallback NeoForgeSlottedStorage(handler)
            }

            null
        }

        ItemStorage.ITEM.registerFallback { stack, context ->
            if (stack == null)
                return@registerFallback null

            val handler = Capabilities.ItemHandler.ITEM.getCapability(stack, null)

            if (handler != null && handler !is FabricItemStorageCapability) {
                return@registerFallback NeoForgeSlottedStorage(handler)
            }

            null
        }

        FluidStorage.SIDED.registerFallback { world, pos, state, blockEntity, direction ->
            if (blockEntity == null)
                return@registerFallback null

            val handler = Capabilities.FluidHandler.BLOCK.getCapability(world, pos, state, blockEntity, direction)

            if (handler != null && handler !is FabricFluidStorageCapability) {
                return@registerFallback NeoForgeFluidStorage(handler)
            }

            null
        }

        FluidStorage.ITEM.registerFallback { itemStack, context ->
            if (itemStack == null || itemStack.isEmpty)
                return@registerFallback null

            val handler = Capabilities.FluidHandler.ITEM.getCapability(itemStack, null)

            if (handler != null && handler !is FabricFluidItemStorageCapability) {
                return@registerFallback NeoForgeFluidStorage(handler)
            }

            null
        }

        EnergyStorage.SIDED.registerFallback { world, pos, state, blockEntity, direction ->
            if (blockEntity == null)
                return@registerFallback null

            val handler = Capabilities.EnergyStorage.BLOCK.getCapability(world, pos, state, blockEntity, direction)

            if (handler != null && handler !is FabricEnergyStorageCapability) {
                return@registerFallback NeoForgeEnergyStorage(handler)
            }

            null
        }

        EnergyStorage.ITEM.registerFallback { itemStack, context ->
            if (itemStack == null || itemStack.isEmpty)
                return@registerFallback null

            val handler = Capabilities.EnergyStorage.ITEM.getCapability(itemStack, null)

            if (handler != null && handler !is FabricEnergyStorageCapability) {
                return@registerFallback NeoForgeEnergyStorage(handler)
            }

            null
        }

        // Fabric Lookups inside NeoForge Capabilities
        Capabilities.ItemHandler.BLOCK.providers = AlternativeCapabilityMap(Capabilities.ItemHandler.BLOCK.providers) {
            mutableListOf(
                FabricLookupAsSlottedBlockCapabilityProvider(it, ItemStorage.SIDED, { s -> s is NeoForgeSlottedStorage }, ::FabricItemStorageCapability)
            )
        }
        Capabilities.ItemHandler.ITEM.providers = AlternativeCapabilityMap(Capabilities.ItemHandler.ITEM.providers) {
            mutableListOf(
                FabricLookupAsSlottedItemCapabilityProvider(it, ItemStorage.ITEM, { stack -> ContainerItemContext.withConstant(stack) }, { s -> s is NeoForgeSlottedStorage }) { storage, _ -> FabricItemStorageCapability(storage) }
            )
        }

        Capabilities.FluidHandler.BLOCK.providers = AlternativeCapabilityMap(Capabilities.FluidHandler.BLOCK.providers) {
            mutableListOf(
                FabricLookupAsSlottedBlockCapabilityProvider(it, FluidStorage.SIDED, { s -> s is NeoForgeFluidStorage }, ::FabricFluidStorageCapability)
            )
        }
        Capabilities.FluidHandler.ITEM.providers = AlternativeCapabilityMap(Capabilities.FluidHandler.ITEM.providers) {
            mutableListOf(
                FabricLookupAsItemCapabilityProvider(it, FluidStorage.ITEM, { stack -> MutableContainerItemContext(stack) }, { s -> s is NeoForgeFluidStorage }, ::FabricFluidItemStorageCapability)
            )
        }

        Capabilities.EnergyStorage.BLOCK.providers = AlternativeCapabilityMap(Capabilities.EnergyStorage.BLOCK.providers) {
            mutableListOf(
                FabricLookupAsBlockCapabilityProvider(it, EnergyStorage.SIDED, { s -> s is NeoForgeEnergyStorage }, ::FabricEnergyStorageCapability)
            )
        }
        Capabilities.EnergyStorage.ITEM.providers = AlternativeCapabilityMap(Capabilities.EnergyStorage.ITEM.providers) {
            mutableListOf(
                FabricLookupAsItemCapabilityProvider(it, EnergyStorage.ITEM, { stack -> ContainerItemContext.withConstant(stack) }, { s -> s is NeoForgeEnergyStorage }) { storage, _ -> FabricEnergyStorageCapability(storage) }
            )
        }
    }

    private var <T, C> BlockCapability<T, C>.providers: MutableMap<Block, MutableList<IBlockCapabilityProvider<T, C>>>
        get() = (this as BlockCapabilityAccessor<T, C>).`sporran$getProviders`()
        set(value) = (this as BlockCapabilityAccessor<T, C>).`sporran$setProviders`(value)

    private var <T, C> EntityCapability<T, C>.providers: MutableMap<EntityType<*>, MutableList<ICapabilityProvider<Entity, C, T>>>
        get() = (this as EntityCapabilityAccessor<T, C>).`sporran$getProviders`()
        set(value) = (this as EntityCapabilityAccessor<T, C>).`sporran$setProviders`(value)

    private var <T, C> ItemCapability<T, C>.providers: MutableMap<Item, MutableList<ICapabilityProvider<ItemStack, C, T>>>
        get() = (this as ItemCapabilityAccessor<T, C>).`sporran$getProviders`()
        set(value) = (this as ItemCapabilityAccessor<T, C>).`sporran$setProviders`(value)

    /*@SubscribeEvent
    fun onAttachBlockEntityCapabilities(event: AttachCapabilitiesEvent<BlockEntity>) {
        val blockEntity = event.`object`
        if (ItemStorage.SIDED.getProvider(blockEntity.blockState.block) != null)
            event.addCapability(ResourceLocation(Sporran.MOD_ID, "fabric_item_storage"),
                FabricItemStorageCapabilityProvider(blockEntity)
            )

        if (FluidStorage.SIDED.getProvider(blockEntity.blockState.block) != null)
            event.addCapability(ResourceLocation(Sporran.MOD_ID, "fabric_fluid_storage"),
                FabricFluidStorageCapabilityProvider(blockEntity)
            )

        if (EnergyStorage.SIDED.getProvider(blockEntity.blockState.block) != null)
            event.addCapability(ResourceLocation(Sporran.MOD_ID, "fabric_energy_storage"),
                FabricEnergyStorageCapabilityProvider(blockEntity)
            )
    }

    @SubscribeEvent
    fun onAttachItemStackCapabilities(event: AttachCapabilitiesEvent<ItemStack>) {
        val stack = event.`object`
        if (ItemItemStorages.ITEM.getProvider(stack.item) != null)
            event.addCapability(ResourceLocation(Sporran.MOD_ID, "fabric_item_item_storage"),
                FabricItemItemStorageCapabilityProvider(stack)
            )

        if (FluidStorage.ITEM.getProvider(stack.item) != null)
            event.addCapability(ResourceLocation(Sporran.MOD_ID, "fabric_fluid_item_storage"),
                FabricFluidItemStorageCapabilityProvider(stack)
            )

        if (EnergyStorage.ITEM.getProvider(stack.item) != null)
            event.addCapability(ResourceLocation(Sporran.MOD_ID, "fabric_energy_item_storage"),
                FabricEnergyItemStorageCapabilityProvider(stack)
            )
    }*/

    companion object {
        const val REBORN_ENERGY_TO_FORGE_ENERGY = 10 // 1 E -> 10 FE, matching Connector Extras
    }
}
