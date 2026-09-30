package dev.sporran.injections.network;

import net.minecraft.network.PacketListener;
import net.minecraft.network.ProtocolInfo;
import dev.sporran.util.SporranHelper;

public interface PacketEncoderInjection<T extends PacketListener> {
    default ProtocolInfo<T> getProtocolInfo() {
        throw SporranHelper.createMixinException(PacketEncoderInjection.class, "getProtocolInfo");
    }
}
