// TRACKED HASH: 820cf3db05b769c92b345f8be864731738c0d340
/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

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