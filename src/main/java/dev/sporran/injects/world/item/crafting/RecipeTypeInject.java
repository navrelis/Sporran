// TRACKED HASH: b6a7b3f4dcd24203dabc8d78fcec1330affbe661
/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.item.crafting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.helpers.mixin.CreateStatic;
import dev.sporran.injections.world.item.crafting.RecipeTypeInjection;

@Mixin(RecipeType.class)
public interface RecipeTypeInject extends RecipeTypeInjection {
    @CreateStatic
    private static <T extends Recipe<?>> RecipeType<T> simple(ResourceLocation name) {
        return RecipeTypeInjection.simple(name);
    }
}