package dev.sporran.injects.world.item.component;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import dev.sporran.util.SporranHelper;

@Mixin(BundleContents.class)
public abstract class BundleContentsInject {
    @Mixin(BundleContents.Mutable.class)
    public abstract static class MutableInject {
        @WrapOperation(method = "tryInsert", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;canFitInsideContainerItems()Z"))
        private boolean sporran$checkCanFitUsingStack(Item instance, Operation<Boolean> original, @Local(argsOnly = true) ItemStack stack) {
            if (SporranHelper.INSTANCE.hasMethodOverride(stack.getItem().getClass(), Item.class, "canFitInsideContainerItems", ItemStack.class)) {
                return stack.canFitInsideContainerItems();
            }

            return original.call(instance);
        }
    }
}
