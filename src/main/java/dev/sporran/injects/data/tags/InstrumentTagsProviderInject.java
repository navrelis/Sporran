package dev.sporran.injects.data.tags;

import net.minecraft.data.tags.InstrumentTagsProvider;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(InstrumentTagsProvider.class)
public abstract class InstrumentTagsProviderInject {
    // Sporran: we have no reason to implement this
}
