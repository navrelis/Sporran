package dev.sporran.injects.server.level;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.neoforged.neoforge.attachment.AttachmentSync;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.network.bundle.PacketAndPayloadAcceptor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dev.sporran.injections.server.level.ServerEntityInjection;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;

@Mixin(ServerEntity.class)
public abstract class ServerEntityInject implements ServerEntityInjection {
    @Shadow @Final private Entity entity;

    @Shadow
    public abstract void sendPairingData(ServerPlayer player, Consumer<Packet<ClientGamePacketListener>> consumer);

    // I don't actually know how to handle instanceof properly
    @Definition(id = "itemStack", local = @Local(type = ItemStack.class))
    @Definition(id = "getItem", method = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;")
    @Definition(id = "MapItem", type = MapItem.class)
    @Expression("itemStack.getItem() instanceof MapItem")
    @Redirect(method = "sendChanges", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean sporran$forceHandleAllItems(Object instance, Class<?> type) {
        return true;
    }

    @Inject(method = "removePairing", at = @At("TAIL"))
    private void sporran$callStopEntityTracking(ServerPlayer player, CallbackInfo ci) {
        EventHooks.onStopEntityTracking(this.entity, player);
    }

    @Unique private PacketAndPayloadAcceptor<ClientGamePacketListener> sporran$payloadAcceptor;
    @Unique private final AtomicBoolean sporran$isDeferringPairingData = new AtomicBoolean(false);

    @WrapOperation(method = "addPairing", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerEntity;sendPairingData(Lnet/minecraft/server/level/ServerPlayer;Ljava/util/function/Consumer;)V"))
    private void sporran$wrapWithPacketAndPayloadAcceptor(ServerEntity instance, ServerPlayer player, Consumer<Packet<ClientGamePacketListener>> consumer, Operation<Void> original) {
        this.sporran$payloadAcceptor = new PacketAndPayloadAcceptor<>((Consumer<Packet<? super ClientGamePacketListener>>) (Object) consumer); // why?
        original.call(instance, player, consumer);
        this.sporran$payloadAcceptor = null;
    }

    @Inject(method = "addPairing", at = @At("TAIL"))
    private void sporran$callStartEntityTracking(ServerPlayer player, CallbackInfo ci) {
        EventHooks.onStartEntityTracking(this.entity, player);
    }

    @Unique
    public void neoforge$sendPairingData(ServerPlayer player, PacketAndPayloadAcceptor<ClientGamePacketListener> acceptor) {
        if (this.sporran$isDeferringPairingData.getAndSet(false)) {
            return;
        }

        this.sendPairingData(player, acceptor);
    }

    @Override
    public void sendPairingData(ServerPlayer player, PacketAndPayloadAcceptor<ClientGamePacketListener> acceptor) {
        this.sporran$payloadAcceptor = acceptor;
        this.sendPairingData(player, acceptor::accept);
        this.sporran$payloadAcceptor = null;
    }

    @Inject(method = "sendPairingData", at = @At("HEAD"))
    private void sporran$tryHandleFunnyMixins(ServerPlayer player, Consumer<Packet<ClientGamePacketListener>> consumer, CallbackInfo ci) {
        try {
            this.sporran$isDeferringPairingData.set(true);

            this.neoforge$sendPairingData(player, Objects.requireNonNullElseGet(this.sporran$payloadAcceptor,
                () -> new PacketAndPayloadAcceptor<>((Consumer<Packet<? super ClientGamePacketListener>>) (Object) consumer))
            );
        } finally {
            this.sporran$isDeferringPairingData.set(false);
        }
    }

    @Definition(id = "trackedDataValues", field = "Lnet/minecraft/server/level/ServerEntity;trackedDataValues:Ljava/util/List;")
    @Expression("this.trackedDataValues != null")
    @Inject(method = "sendPairingData", at = @At("MIXINEXTRAS:EXPRESSION"))
    private void sporran$sendPairingDataWithAcceptor(ServerPlayer player, Consumer<Packet<ClientGamePacketListener>> consumer, CallbackInfo ci) {
        if (this.sporran$payloadAcceptor != null) {
            this.entity.sendPairingData(player, this.sporran$payloadAcceptor::accept);
        } else {
            this.entity.sendPairingData(player, payload -> ((Consumer) consumer).accept(new ClientboundCustomPayloadPacket(payload)));
        }
    }

    @Inject(method = "sendPairingData", at = @At("TAIL"))
    private void sporran$handleSyncInitialAttachments(ServerPlayer player, Consumer<Packet<ClientGamePacketListener>> consumer, CallbackInfo ci) {
        AttachmentSync.syncInitialEntityAttachments(this.entity, player, packet -> ((Consumer) consumer).accept(packet));
    }
}
