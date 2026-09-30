// TRACKED HASH: 2bb3ae15b22cc5132705ea07857cbe14b92c3de6
package dev.sporran.injects.server.players;

import java.nio.file.Path;
import java.util.function.Function;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBuf;
import net.neoforged.neoforge.attachment.AttachmentSync;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.player.PlayerRespawnPositionEvent;
import net.neoforged.neoforge.network.payload.ClientboundCustomSetTimePayload;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import dev.sporran.injections.network.RegistryFriendlyByteBufInjection;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.PlayerDataStorage;

@Mixin(PlayerList.class)
public abstract class PlayerListInject {
    @Shadow @Final private PlayerDataStorage playerIo;
    @Shadow @Final private MinecraftServer server;

    @ModifyExpressionValue(method = "placeNewPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/RegistryFriendlyByteBuf;decorator(Lnet/minecraft/core/RegistryAccess;)Ljava/util/function/Function;"))
    private Function<ByteBuf, RegistryFriendlyByteBuf> sporran$addConnectionTypeToBuf(Function<ByteBuf, RegistryFriendlyByteBuf> original, @Local ServerGamePacketListenerImpl packetListener) {
        return RegistryFriendlyByteBufInjection.sporran$wrappedDecorator(packetListener.getConnectionType(), original);
    }

    @Inject(method = "placeNewPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ClientboundUpdateRecipesPacket;<init>(Ljava/util/Collection;)V"))
    private void sporran$syncDatapackRegistries(Connection connection, ServerPlayer player, CommonListenerCookie cookie, CallbackInfo ci) {
        NeoForge.EVENT_BUS.post(new OnDatapackSyncEvent((PlayerList) (Object) this, player));
    }

    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    private void sporran$firePlayerLoginEvent(Connection connection, ServerPlayer player, CommonListenerCookie cookie, CallbackInfo ci) {
        AttachmentSync.syncInitialPlayerAttachments(player);
        EventHooks.firePlayerLoggedIn(player);
    }

    @Inject(method = "load", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;debug(Ljava/lang/String;)V", shift = At.Shift.AFTER))
    private void sporran$firePlayerLoadEvent(ServerPlayer player, CallbackInfoReturnable<CompoundTag> cir) {
        EventHooks.firePlayerLoadingEvent(player, this.playerIo, player.getUUID().toString());
    }

    @Inject(method = "save", at = @At("HEAD"), cancellable = true)
    private void sporran$preventSaveIfNoConnection(ServerPlayer player, CallbackInfo ci) {
        if (player.connection == null)
            ci.cancel();
    }

    @Inject(method = "remove", at = @At("HEAD"))
    private void sporran$firePlayerLogoutEvent(ServerPlayer player, CallbackInfo ci) {
        EventHooks.firePlayerLoggedOut(player);
    }

    @Inject(method = "respawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/portal/DimensionTransition;newLevel()Lnet/minecraft/server/level/ServerLevel;"))
    private void sporran$firePlayerRespawnPositionEvent(ServerPlayer player, boolean keepInventory, Entity.RemovalReason reason, CallbackInfoReturnable<ServerPlayer> cir, @Local LocalRef<DimensionTransition> transition, @Share("event") LocalRef<PlayerRespawnPositionEvent> eventLocalRef) {
        var event = EventHooks.firePlayerRespawnPositionEvent(player, transition.get(), keepInventory);
        eventLocalRef.set(event);
        transition.set(event.getDimensionTransition());
    }

    @ModifyExpressionValue(method = "respawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/portal/DimensionTransition;missingRespawnBlock()Z"))
    private boolean sporran$checkShouldCopyOriginalSpawn(boolean original, @Share("event") LocalRef<PlayerRespawnPositionEvent> eventLocalRef) {
        if (eventLocalRef.get().sporran$hasCopyChanged())
            return eventLocalRef.get().copyOriginalSpawnPosition();

        return original;
    }

    @Inject(method = "respawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;setHealth(F)V", shift = At.Shift.AFTER))
    private void sporran$firePlayerRespawnEvent(ServerPlayer player, boolean keepInventory, Entity.RemovalReason reason, CallbackInfoReturnable<ServerPlayer> cir, @Local(ordinal = 1) ServerPlayer newPlayer) {
        // Sporran: like NeoForge, with the new player (the old one is already removed from the level)
        AttachmentSync.syncInitialPlayerAttachments(newPlayer);
        EventHooks.firePlayerRespawnEvent(newPlayer, keepInventory);
    }

    @Inject(method = "op", at = @At("HEAD"), cancellable = true)
    private void sporran$checkPermissionChanged(GameProfile profile, CallbackInfo ci) {
        if (EventHooks.onPermissionChanged(profile, this.server.getOperatorUserPermissionLevel(), (PlayerList) (Object) this))
            ci.cancel();
    }

    @Inject(method = "deop", at = @At("HEAD"), cancellable = true)
    private void sporran$checkPermissionChangedDeop(GameProfile profile, CallbackInfo ci) {
        if (EventHooks.onPermissionChanged(profile, 0, (PlayerList) (Object) this))
            ci.cancel();
    }

    @ModifyArg(method = "sendLevelInfo", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V", ordinal = 0))
    private Packet<?> sporran$tryUseCustomSetTime(Packet<?> original, @Local(argsOnly = true) ServerPlayer player, @Local(argsOnly = true) ServerLevel level) {
        if (player.connection.hasChannel(ClientboundCustomSetTimePayload.TYPE)) {
            return (new ClientboundCustomSetTimePayload(level.getGameTime(), level.getDayTime(), level.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT), level.getDayTimeFraction(), level.getDayTimePerTick()))
                .toVanillaClientbound();
        }

        return original;
    }

    // Sporran: level attachments are sent on login, respawn and dimension change (all of them call sendLevelInfo)
    @Inject(method = "sendLevelInfo", at = @At("TAIL"))
    private void sporran$syncInitialLevelAttachments(ServerPlayer player, ServerLevel level, CallbackInfo ci) {
        AttachmentSync.syncInitialLevelAttachments(level, player);
    }

    @Inject(method = "getPlayerAdvancements", at = @At("HEAD"), cancellable = true)
    private void sporran$avoidSetPlayerIfFake(ServerPlayer player, CallbackInfoReturnable<PlayerAdvancements> cir) {
        if (player.isFakePlayer()) {
            Path path = this.server.getWorldPath(LevelResource.PLAYER_ADVANCEMENTS_DIR).resolve("_fake.json");
            cir.setReturnValue(new FakePlayer.FakePlayerAdvancements(this.server.getFixerUpper(), (PlayerList) (Object) this, this.server.getAdvancements(), path, player));
        }
    }

    @Inject(method = "reloadResources", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    private void sporran$syncDatapackOnReload(CallbackInfo ci) {
        NeoForge.EVENT_BUS.post(new OnDatapackSyncEvent((PlayerList) (Object) this, null));
    }
}
