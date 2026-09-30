package dev.sporran.injects.server.dedicated;

import net.minecraft.server.dedicated.DedicatedServerProperties;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DedicatedServerProperties.class)
public abstract class DedicatedServerPropertiesInject {
    // Sporran: don't need this
}
