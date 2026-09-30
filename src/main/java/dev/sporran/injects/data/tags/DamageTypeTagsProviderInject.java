package dev.sporran.injects.data.tags;

import net.minecraft.data.tags.DamageTypeTagsProvider;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DamageTypeTagsProvider.class)
public abstract class DamageTypeTagsProviderInject {
    // Sporran: we have no reason to implement this
}
