// TRACKED HASH: 1886f30859644767992d36dcae1264c9b9614cd4
package dev.sporran.injects.server.network;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.ChatDecorator;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ClientInformationUpdatedEvent;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import dev.sporran.injections.client.multiplayer.CommonListenerCookieInjection;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplInject extends ServerCommonPacketListenerImpl {
    @Shadow
    public ServerPlayer player;

    public ServerGamePacketListenerImplInject(MinecraftServer server, Connection connection, CommonListenerCookie cookie) {
        super(server, connection, cookie);
    }

    @WrapOperation(method = "handleMoveVehicle", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;absMoveTo(DDDFF)V"))
    private void sporran$resyncPlayerWithVehicleOnMove(Entity instance, double x, double y, double z, float yRot, float xRot, Operation<Void> original) {
        original.call(instance, x, y, z, yRot, xRot);
        this.resyncPlayerWithVehicle(instance);
    }

    @WrapOperation(method = "handleMoveVehicle", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;checkMovementStatistics(DDD)V"))
    private void sporran$checkRidingStatisticsBecauseNeoBreaksIt(ServerPlayer instance, double dx, double dy, double dz, Operation<Void> original) {
        original.call(instance, dx, dy, dz);
        instance.checkRidingStatistics(dx, dy, dz);
    }

    @Unique
    private void resyncPlayerWithVehicle(Entity vehicle) {
        Vec3 oldPos = this.player.position();
        float yRot = this.player.getYRot();
        float xRot = this.player.getXRot();
        float yHeadRot = this.player.getYHeadRot();

        vehicle.positionRider(this.player);

        // Preserve old rotation and store old position in old x/y/z
        this.player.setYRot(yRot);
        this.player.setXRot(xRot);
        this.player.setYHeadRot(yHeadRot);
        this.player.xo = oldPos.x;
        this.player.yo = oldPos.y;
        this.player.zo = oldPos.z;
    }

    @ModifyExpressionValue(method = "handleMovePlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Abilities;mayfly:Z", opcode = Opcodes.GETFIELD))
    private boolean sporran$checkMayPlayerFly(boolean original) {
        return original || this.player.mayFly();
    }

    @WrapOperation(method = "handlePlayerAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;setItemInHand(Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;)V", ordinal = 0))
    private void sporran$handleLivingSwapHandItemsEvent(ServerPlayer instance, InteractionHand interactionHand, ItemStack itemStack, Operation<Void> original, @Cancellable CallbackInfo ci, @Local LocalRef<ItemStack> offhandStack) {
        var event = CommonHooks.sporran$onLivingSwapHandItems(this.player, offhandStack.get(), itemStack);
        if (event.isCanceled()) {
            ci.cancel();
            return;
        }

        original.call(instance, interactionHand, event.getItemSwappedToOffHand());
        offhandStack.set(event.getItemSwappedToMainHand());
    }

    // Sporran: why does Neo change it to not use the ackBlockChangesUpTo method????

    @ModifyExpressionValue(method = "method_44900", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getChatDecorator()Lnet/minecraft/network/chat/ChatDecorator;"))
    private ChatDecorator sporran$decorateComponentUsingChatSubmittedDecorator(ChatDecorator original) {
        ChatDecorator neoDecorator = CommonHooks.getServerChatSubmittedDecorator();
        return (player, component) -> neoDecorator.decorate(player, original.decorate(player, component));
    }

    // Sporran: NeoForge's EntityInteractSpecific (ServerGamePacketListenerImpl$1#onInteraction) is bridged from Porting Lib
    //  (Sporran.kt), whose mixin fires at the same point. Firing it here as well fired it twice.

    @ModifyExpressionValue(method = "handlePlayerAbilities", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Abilities;mayfly:Z", opcode = Opcodes.GETFIELD))
    private boolean sporran$checkMayPlayerFlyForAbility(boolean original) {
        return original || this.player.mayFly();
    }

    @WrapOperation(method = "handleClientInformation", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;updateOptions(Lnet/minecraft/server/level/ClientInformation;)V"))
    private void sporran$handleUpdateClientInformation(ServerPlayer instance, ClientInformation clientInformation, Operation<Void> original) {
        ClientInformation oldInfo = instance.clientInformation();
        original.call(instance, clientInformation);
        NeoForge.EVENT_BUS.post(new ClientInformationUpdatedEvent(instance, oldInfo, clientInformation));
    }

    @ModifyExpressionValue(method = "handleConfigurationAcknowledged", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;createCookie(Lnet/minecraft/server/level/ClientInformation;)Lnet/minecraft/server/network/CommonListenerCookie;"))
    private CommonListenerCookie sporran$appendConnectionTypeToCookie(CommonListenerCookie original) {
        ((CommonListenerCookieInjection) (Object) original).sporran$setConnectionType(this.getConnectionType());
        return original;
    }

    @Inject(method = "handleCustomPayload", at = @At("HEAD"))
    private void sporran$handleSuperCustomPayload(ServerboundCustomPayloadPacket packet, CallbackInfo ci) {
        // Sporran: yes it does, don't do that
        //super.handleCustomPayload(packet);
    }
}