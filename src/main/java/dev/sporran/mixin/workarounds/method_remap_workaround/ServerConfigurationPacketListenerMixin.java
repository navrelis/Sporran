/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.mixin.workarounds.method_remap_workaround;

import net.neoforged.neoforge.common.extensions.IServerConfigurationPacketListenerExtension;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.workarounds.IServerConfigurationPacketListenerWorkaround;

import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener;
import net.minecraft.server.network.ConfigurationTask;


@Implements(@Interface(iface = IServerConfigurationPacketListenerWorkaround.class, prefix = "sporran$i$"))
@Mixin(ServerConfigurationPacketListener.class)
public interface ServerConfigurationPacketListenerMixin extends IServerConfigurationPacketListenerExtension {
    @Intrinsic
    default void sporran$i$finishCurrentTask(ConfigurationTask.Type task) {
        this.finishCurrentTask(task);
    }
}
