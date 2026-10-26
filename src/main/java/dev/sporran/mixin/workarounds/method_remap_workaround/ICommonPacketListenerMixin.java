/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

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
