package dev.sporran.injects.server.packs.resources;

import net.minecraft.server.packs.resources.MultiPackResourceManager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MultiPackResourceManager.class)
public abstract class MultiPackResourceManagerInject {
    // Sporran: don't think we need this
}
