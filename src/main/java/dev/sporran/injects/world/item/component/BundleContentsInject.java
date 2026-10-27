/*
 * Changed by the Sporran project between 2026-09-29 and 2026-10-27; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.item.component;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import dev.sporran.util.SporranHelper;

@Mixin(BundleContents.class)
public abstract class BundleContentsInject {
    @Mixin(BundleContents.Mutable.class)
    public abstract static class MutableInject {
        @WrapOperation(method = "tryInsert", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;canFitInsideContainerItems()Z"))
        private boolean sporran$checkCanFitUsingStack(Item instance, Operation<Boolean> original, @Local(argsOnly = true) ItemStack stack) {
            // Sporran: asked against IItemExtension, where the stack-aware method is a default (against Item.class the check was true for every item).
            if (SporranHelper.INSTANCE.hasMethodOverride(stack.getItem().getClass(), IItemExtension.class, "canFitInsideContainerItems", ItemStack.class)) {
                return stack.canFitInsideContainerItems();
            }

            return original.call(instance);
        }
    }
}
