/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

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
