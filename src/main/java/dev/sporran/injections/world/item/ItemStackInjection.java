/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.item;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;
import dev.sporran.util.SporranHelper;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public interface ItemStackInjection {
    default boolean isComponentsPatchEmpty() {
        throw new IllegalStateException();
    }

    default void hurtAndBreak(int damage, ServerLevel level, @Nullable LivingEntity entity, Consumer<Item> onBreak) {
        throw SporranHelper.createMixinException(ItemStackInjection.class, "hurtAndBreak");
    }

    default ItemEnchantments getTagEnchantments() {
        throw SporranHelper.createMixinException(ItemStackInjection.class, "getTagEnchantments");
    }
}
