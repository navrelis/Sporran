package dev.sporran.helpers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;

import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.protocol.PacketFlow;

public interface StupidWorkarounds {
    // for CustomPacketPayloadInjection - because CreativeCore just doesn't work properly otherwise, due to reflection bullshit. it's dumb.
    ThreadLocal<ConnectionProtocol> sporran$protocol = new ThreadLocal<>();
    ThreadLocal<PacketFlow> sporran$packetFlow = new ThreadLocal<>();

    Map<FluidType, IClientFluidTypeExtensions> sporran$fabricFluidExtensions = Collections.synchronizedMap(new HashMap<>());
}
