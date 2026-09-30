package dev.sporran.injections.server.level;

import net.neoforged.neoforge.network.bundle.PacketAndPayloadAcceptor;
import dev.sporran.util.SporranHelper;

import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;

public interface ServerEntityInjection {
    default void sendPairingData(ServerPlayer player, PacketAndPayloadAcceptor<ClientGamePacketListener> acceptor) {
        throw SporranHelper.createMixinException(ServerEntityInjection.class, "sendPairingData");
    }
}
