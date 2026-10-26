/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.item.crafting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public interface RecipeTypeInjection {
    static <T extends Recipe<?>> RecipeType<T> simple(ResourceLocation name) {
        var serialized = name.toString();

        return new RecipeType<T>() {
            @Override
            public String toString() {
                return serialized;
            }
        };
    }
}
