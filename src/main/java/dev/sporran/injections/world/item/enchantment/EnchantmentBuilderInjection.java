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
