package dev.sporran.injects.data.tags;

import net.minecraft.data.tags.GameEventTagsProvider;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GameEventTagsProvider.class)
public abstract class GameEventTagsProviderInject {
    // Sporran: we have no reason to implement this
}
