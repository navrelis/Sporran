package dev.sporran.injects.data.tags;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.data.tags.EnchantmentTagsProvider;

@Mixin(EnchantmentTagsProvider.class)
public abstract class EnchantmentTagsProviderInject {
    // Sporran: we have no reason to implement this
}
