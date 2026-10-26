/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.network.protocol.common.custom;

import java.util.List;

import dev.sporran.helpers.StupidWorkarounds;

import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface CustomPacketPayloadInjection {
    static <B extends FriendlyByteBuf> StreamCodec<B, CustomPacketPayload> codec(
        final CustomPacketPayload.FallbackProvider<B> fallbackProvider, List<CustomPacketPayload.TypeAndCodec<? super B, ?>> payloads,
        ConnectionProtocol protocol, PacketFlow packetFlow
    ) {
        StupidWorkarounds.sporran$protocol.set(protocol);
        StupidWorkarounds.sporran$packetFlow.set(packetFlow);

        try {
            return CustomPacketPayload.codec(fallbackProvider, payloads);
        } finally {
            StupidWorkarounds.sporran$protocol.remove();
            StupidWorkarounds.sporran$packetFlow.remove();
        }
    }

    default ClientboundCustomPayloadPacket toVanillaClientbound() {
        return new ClientboundCustomPayloadPacket((CustomPacketPayload) this);
    }

    default ServerboundCustomPayloadPacket toVanillaServerbound() {
        return new ServerboundCustomPayloadPacket((CustomPacketPayload) this);
    }
}
