/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.network.connection.ConnectionType;
import dev.sporran.util.SporranHelper;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public interface RegistryFriendlyByteBufInjection {
    static Function<ByteBuf, RegistryFriendlyByteBuf> decorator(RegistryAccess registryAccess, ConnectionType connectionType) {
        return buf -> {
            var friendlyBuf = new RegistryFriendlyByteBuf(buf, registryAccess);
            friendlyBuf.sporran$setConnectionType(connectionType);

            return friendlyBuf;
        };
    }

    static Function<ByteBuf, RegistryFriendlyByteBuf> sporran$wrappedDecorator(ConnectionType connectionType, Function<ByteBuf, RegistryFriendlyByteBuf> wrapped) {
        return buf -> {
            var original = wrapped.apply(buf);
            original.sporran$setConnectionType(connectionType);
            return original;
        };
    }

    default ConnectionType getConnectionType() {
        throw SporranHelper.createMixinException(RegistryFriendlyByteBufInjection.class, "getConnectionType");
    }

    default void sporran$setConnectionType(ConnectionType connectionType) {
        throw SporranHelper.createMixinException(RegistryFriendlyByteBufInjection.class, "sporran$setConnectionType");
    }
}
