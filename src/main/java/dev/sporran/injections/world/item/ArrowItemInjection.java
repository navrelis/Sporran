/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import dev.sporran.util.SporranHelper;

public interface ArrowItemInjection {
    default boolean isInfinite(ItemStack stack, ItemStack bow, LivingEntity entity) {
        throw SporranHelper.createMixinException(ArrowItemInjection.class, "isInfinite");
    }
}
