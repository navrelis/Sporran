package dev.sporran.mixin.workarounds.method_remap_workaround;

import net.neoforged.neoforge.common.extensions.ICommonPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.workarounds.ICommonPacketListenerWorkaround;

import net.minecraft.network.protocol.Packet;

@Mixin(ICommonPacketListener.class)
public interface ICommonPacketListenerMixin extends ICommonPacketListenerWorkaround {
    @Override
    void send(Packet<?> packet);
}
