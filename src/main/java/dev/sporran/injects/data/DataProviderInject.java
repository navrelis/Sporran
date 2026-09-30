package dev.sporran.injects.data;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.data.DataProvider;

@Mixin(DataProvider.class)
public interface DataProviderInject {
    // Sporran: is this actually needed?
}
