package dev.sporran.injects.data.tags;

import net.minecraft.data.tags.WorldPresetTagsProvider;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WorldPresetTagsProvider.class)
public abstract class WorldPresetTagsProviderInject {
    // Sporran: we have no reason to implement this
}
