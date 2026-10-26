/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.stats;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.inventory.RecipeBookType;
import dev.sporran.mixin.RecipeBookSettingsAccessor;

public interface RecipeBookSettingsInjection {
    static void addTagsForType(RecipeBookType type, String openTag, String filteringTag) {
        RecipeBookSettingsAccessor.getTagFields().put(type, new Pair<>(openTag, filteringTag));
    }
}
