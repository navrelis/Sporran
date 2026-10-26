/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.item.crafting;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RepairItemRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RepairItemRecipe.class)
public abstract class RepairItemRecipeInject {
    @ModifyReturnValue(method = "canCombine", at = @At("RETURN"))
    private static boolean sporran$checkIsBothRepairable(boolean original, ItemStack first, ItemStack second) {
        return original && first.isRepairable() && second.isRepairable();
    }
}
