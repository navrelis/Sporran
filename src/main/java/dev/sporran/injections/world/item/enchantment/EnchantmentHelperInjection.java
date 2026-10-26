/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.item.enchantment;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public interface EnchantmentHelperInjection {
    ThreadLocal<Boolean> sporran$shouldUseTagEnchantment = ThreadLocal.withInitial(() -> false);

    static int getTagEnchantmentLevel(Holder<Enchantment> enchantment, ItemStack stack) {
        sporran$shouldUseTagEnchantment.set(true);
        var value = EnchantmentHelper.getItemEnchantmentLevel(enchantment, stack);
        sporran$shouldUseTagEnchantment.remove();
        return value;
    }
}
