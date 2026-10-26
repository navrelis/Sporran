/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.mixin.debug;

import com.moulberry.mixinconstraints.annotations.IfDevEnvironment;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.multiplayer.ClientPacketListener;

@IfDevEnvironment
@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
    /*@WrapOperation(method = "handleBundlePacket", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/Packet;handle(Lnet/minecraft/network/PacketListener;)V"))
    private <T extends PacketListener> void sporran$debug$actuallyComplainIfAnErrorOccurs(Packet<T> instance, T t, Operation<Void> original) {
        try {
            original.call(instance, t);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }*/
}
