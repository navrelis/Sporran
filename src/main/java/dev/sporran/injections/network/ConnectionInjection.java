/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.network;

import io.netty.channel.Channel;
import net.minecraft.network.Connection;
import net.minecraft.network.ProtocolInfo;
import net.minecraft.network.protocol.PacketFlow;
import dev.sporran.processor.FabricInjectedInterface;
import dev.sporran.util.SporranHelper;

import java.util.function.Consumer;

@FabricInjectedInterface(Connection.class)
public interface ConnectionInjection {
    default Channel channel() {
        throw new RuntimeException("mixin, why didn't you add this");
    }

    default PacketFlow getDirection() {
        throw new RuntimeException("mixin, why didn't you add this");
    }

    default ProtocolInfo<?> getInboundProtocol() {
        throw SporranHelper.createMixinException(ConnectionInjection.class, "getInboundProtocol");
    }
}
