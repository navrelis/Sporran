/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.item.crafting;

import com.mojang.serialization.Codec;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.WithConditions;

import java.util.Optional;

public interface RecipeInjection {
    Codec<Optional<WithConditions<Recipe<?>>>> CONDITIONAL_CODEC = ConditionalOps.createConditionalCodecWithConditions(Recipe.CODEC);
}
