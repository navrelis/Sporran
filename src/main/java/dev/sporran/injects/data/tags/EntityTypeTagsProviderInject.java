package dev.sporran.injects.data.tags;

import net.minecraft.data.tags.EntityTypeTagsProvider;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityTypeTagsProvider.class)
public abstract class EntityTypeTagsProviderInject {
    // Sporran: we have no reason to implement this
}
