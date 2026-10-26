/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.item.enchantment;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.enchantment.Enchantment;
import dev.sporran.util.SporranHelper;

import java.util.function.UnaryOperator;

public interface EnchantmentBuilderInjection {
    default Enchantment.Builder withCustomName(UnaryOperator<MutableComponent> nameFactory) {
        throw SporranHelper.createMixinException(EnchantmentBuilderInjection.class, "withCustomName");
    }
}
