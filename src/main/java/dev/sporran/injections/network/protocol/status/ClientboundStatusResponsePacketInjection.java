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
