/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.item.crafting;

import net.minecraft.world.item.crafting.BookCloningRecipe;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BookCloningRecipe.class)
public abstract class BookCloningRecipeInject {
    // Sporran: give me one good reason why this patch needs to be implemented
}
