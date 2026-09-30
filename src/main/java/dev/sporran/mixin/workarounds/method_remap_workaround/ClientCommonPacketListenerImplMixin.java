package dev.sporran.mixin.workarounds.method_remap_workaround;

import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import dev.sporran.workarounds.ICommonPacketListenerWorkaround;

import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.network.protocol.Packet;

@Implements(@Interface(iface = ICommonPacketListenerWorkaround.class, prefix = "sporran$i$", remap = Interface.Remap.NONE))
@Mixin(ClientCommonPacketListenerImpl.class)
public abstract class ClientCommonPacketListenerImplMixin {
    @Shadow
    public abstract void send(Packet<?> packet);

    @Intrinsic
    public void sporran$i$send(Packet<?> packet) {
        this.send(packet);
    }
}
