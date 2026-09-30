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
