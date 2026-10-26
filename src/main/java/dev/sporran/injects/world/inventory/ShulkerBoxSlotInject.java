/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.inventory;

import net.minecraft.world.inventory.ShulkerBoxSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import dev.sporran.util.SporranHelper;

@Mixin(ShulkerBoxSlot.class)
public abstract class ShulkerBoxSlotInject {
    @Inject(method = "mayPlace", at = @At("HEAD"), cancellable = true)
    private void sporran$tryUseStackAwareCheck(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (SporranHelper.INSTANCE.hasMethodOverride(stack.getItem().getClass(), Item.class, "canFitInsideContainerItems", ItemStack.class)) {
            cir.setReturnValue(stack.canFitInsideContainerItems());
        }
    }
}
