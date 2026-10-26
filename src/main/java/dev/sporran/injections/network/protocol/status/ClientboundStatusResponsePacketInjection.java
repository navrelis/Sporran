/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.network.protocol.status;

import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
import net.minecraft.network.protocol.status.ServerStatus;
import org.jetbrains.annotations.Nullable;
import dev.sporran.processor.FabricInjectedInterface;

@FabricInjectedInterface(ClientboundStatusResponsePacket.class)
public interface ClientboundStatusResponsePacketInjection {
    static ClientboundStatusResponsePacket create(ServerStatus status, @Nullable String cachedStatus) {
        var packet = new ClientboundStatusResponsePacket(status);
        ((ClientboundStatusResponsePacketInjection) (Object) packet).sporran$setCachedStatus(cachedStatus);

        return packet;
    }

    void sporran$setCachedStatus(String data);
    @Nullable String cachedStatus();
}
