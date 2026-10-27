/*
 * Changed by the Sporran project between 2026-09-29 and 2026-10-27; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.inventory;

import net.minecraft.world.inventory.ShulkerBoxSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import dev.sporran.util.SporranHelper;
import net.neoforged.neoforge.common.extensions.IItemExtension;

@Mixin(ShulkerBoxSlot.class)
public abstract class ShulkerBoxSlotInject {
    @Inject(method = "mayPlace", at = @At("HEAD"), cancellable = true)
    private void sporran$tryUseStackAwareCheck(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        // Sporran: asked against IItemExtension, where the stack-aware method is a default (against Item.class the check
        //  was true for every item, so vanilla's mayPlace never ran).
        if (SporranHelper.INSTANCE.hasMethodOverride(stack.getItem().getClass(), IItemExtension.class, "canFitInsideContainerItems", ItemStack.class)) {
            cir.setReturnValue(stack.canFitInsideContainerItems());
        }
    }
}
