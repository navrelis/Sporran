package dev.sporran.injects.server.network;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.server.network.ServerStatusPacketListenerImpl;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import dev.sporran.helpers.mixin.CreateInitializer;
import dev.sporran.injections.network.protocol.status.ClientboundStatusResponsePacketInjection;
import dev.sporran.injections.server.network.ServerStatusPacketListenerImplInjection;

@Mixin(ServerStatusPacketListenerImpl.class)
public abstract class ServerStatusPacketListenerImplInject implements ServerStatusPacketListenerImplInjection {
    @Unique @Nullable
    private String statusCache;

    public ServerStatusPacketListenerImplInject(ServerStatus status, Connection connection) {}

    @CreateInitializer
    public ServerStatusPacketListenerImplInject(ServerStatus status, Connection connection, @Nullable String statusCache) {
        this(status, connection);
        this.statusCache = statusCache;
    }

    @Override
    public void sporran$setStatusCache(String statusCache) {
        this.statusCache = statusCache;
    }

    @ModifyExpressionValue(method = "handleStatusRequest", at = @At(value = "NEW", target = "(Lnet/minecraft/network/protocol/status/ServerStatus;)Lnet/minecraft/network/protocol/status/ClientboundStatusResponsePacket;"))
    private ClientboundStatusResponsePacket sporran$addStatusCacheToRequest(ClientboundStatusResponsePacket original) {
        ((ClientboundStatusResponsePacketInjection) (Object) original).sporran$setCachedStatus(this.statusCache);

        return original;
    }
}
