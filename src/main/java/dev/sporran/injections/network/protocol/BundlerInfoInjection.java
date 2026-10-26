/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.network.protocol;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.protocol.Packet;
import dev.sporran.util.SporranHelper;

import java.util.function.Consumer;

public interface BundlerInfoInjection {
    default void unbundlePacket(Packet<?> bundlePacket, Consumer<Packet<?>> packetSender, ChannelHandlerContext context) {
        throw SporranHelper.createMixinException(BundlerInfoInjection.class, "unbundlePacket");
    }
}
