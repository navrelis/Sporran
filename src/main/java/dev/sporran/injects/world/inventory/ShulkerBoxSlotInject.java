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
