/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.server.network;

import net.minecraft.server.network.CommonListenerCookie;
import net.neoforged.neoforge.network.connection.ConnectionType;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.injections.server.network.CommonListenerCookieInjection;

@Mixin(CommonListenerCookie.class)
public abstract class CommonListenerCookieInject implements CommonListenerCookieInjection {
    private ConnectionType connectionType = ConnectionType.OTHER;

    @Override
    public ConnectionType connectionType() {
        return this.connectionType;
    }

    @Override
    public void sporran$setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }
}
