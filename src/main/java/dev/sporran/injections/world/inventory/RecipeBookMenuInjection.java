/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.inventory;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.RecipeBookCategories;

import java.util.List;

public interface RecipeBookMenuInjection {
    @Environment(EnvType.CLIENT)
    default List<RecipeBookCategories> getRecipeBookCategories() {
        throw new IllegalStateException();
    }
}
