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
