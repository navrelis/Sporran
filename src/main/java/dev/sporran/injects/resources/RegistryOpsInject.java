package dev.sporran.injects.resources;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.helpers.mixin.CreateStatic;
import dev.sporran.injections.resources.RegistryOpsInjection;

@Mixin(RegistryOps.class)
public abstract class RegistryOpsInject {
    @CreateStatic
    private static <E> MapCodec<HolderLookup.RegistryLookup<E>> retrieveRegistryLookup(ResourceKey<? extends Registry<? extends E>> registryKey) {
        return RegistryOpsInjection.retrieveRegistryLookup(registryKey);
    }
}
