// TRACKED HASH: 820cf3db05b769c92b345f8be864731738c0d340
package dev.sporran.injects.tags;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.helpers.mixin.CreateStatic;
import dev.sporran.injections.tags.FluidTagsInjection;

@Mixin(FluidTags.class)
public class FluidTagsInject implements FluidTagsInjection {
    @CreateStatic
    private static TagKey<Fluid> create(ResourceLocation name) {
        return FluidTagsInjection.create(name);
    }
}