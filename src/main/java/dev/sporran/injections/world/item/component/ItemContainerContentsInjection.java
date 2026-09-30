package dev.sporran.injections.world.item.component;

import net.minecraft.world.item.ItemStack;
import dev.sporran.util.SporranHelper;

public interface ItemContainerContentsInjection {
    default int getSlots() {
        throw SporranHelper.createMixinException(ItemContainerContentsInjection.class, "getSlots");
    }

    default ItemStack getStackInSlot(int slot) {
        throw SporranHelper.createMixinException(ItemContainerContentsInjection.class, "getStackInSlot");
    }
}
