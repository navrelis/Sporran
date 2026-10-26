/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.network;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import dev.sporran.injections.network.ConnectionProtocolInjection;

import net.minecraft.network.ConnectionProtocol;

@Mixin(ConnectionProtocol.class)
public abstract class ConnectionProtocolInject implements ConnectionProtocolInjection {
    @Shadow @Final public static ConnectionProtocol PLAY;
    @Shadow @Final public static ConnectionProtocol CONFIGURATION;

    @Override
    public boolean isPlay() {
        return (Object) this == PLAY;
    }

    @Override
    public boolean isConfiguration() {
        return (Object) this == CONFIGURATION;
    }
}
