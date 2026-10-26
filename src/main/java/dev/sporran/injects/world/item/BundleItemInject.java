/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.item;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BundleItem.class)
public abstract class BundleItemInject {
    @Definition(id = "SECONDARY", field = "Lnet/minecraft/world/inventory/ClickAction;SECONDARY:Lnet/minecraft/world/inventory/ClickAction;")
    @Expression("? != SECONDARY")
    @ModifyExpressionValue(method = "overrideStackedOnOther", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean sporran$checkIsCountNotOne(boolean original, @Local(argsOnly = true) ItemStack stack) {
        return stack.getCount() != 1 || original;
    }

    @Inject(method = "overrideOtherStackedOnMe", at = @At("HEAD"), cancellable = true)
    private void sporran$checkIsCountNotOne(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access, CallbackInfoReturnable<Boolean> cir) {
        if (stack.getCount() != 1)
            cir.setReturnValue(false);
    }
}
