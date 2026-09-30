package dev.sporran.injects.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.network.connection.ConnectionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import dev.sporran.helpers.mixin.CreateInitializer;
import dev.sporran.helpers.mixin.CreateStatic;
import dev.sporran.injections.network.RegistryFriendlyByteBufInjection;

import java.util.function.Function;

@Mixin(RegistryFriendlyByteBuf.class)
public abstract class RegistryFriendlyByteBufInject implements RegistryFriendlyByteBufInjection {
    private ConnectionType connectionType = ConnectionType.OTHER;

    public RegistryFriendlyByteBufInject(ByteBuf byteBuf, RegistryAccess registryAccess) {}

    @CreateInitializer
    public RegistryFriendlyByteBufInject(ByteBuf byteBuf, RegistryAccess registryAccess, ConnectionType connectionType) {
        this(byteBuf, registryAccess);
        this.connectionType = connectionType;
    }

    @Override
    public ConnectionType getConnectionType() {
        return this.connectionType;
    }

    @Override
    public void sporran$setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    @CreateStatic
    private static Function<ByteBuf, RegistryFriendlyByteBuf> decorator(RegistryAccess registryAccess, ConnectionType connectionType) {
        return RegistryFriendlyByteBufInjection.decorator(registryAccess, connectionType);
    }
}
