package dev.sporran.injects.data.loot;

import net.minecraft.data.loot.EntityLootSubProvider;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityLootSubProvider.class)
public abstract class EntityLootSubProviderInject {
    // Sporran: we have no reason to implement this
}
