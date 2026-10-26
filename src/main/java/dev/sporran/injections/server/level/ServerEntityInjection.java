/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

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
