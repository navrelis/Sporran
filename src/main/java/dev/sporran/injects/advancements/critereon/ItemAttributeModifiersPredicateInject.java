/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.advancements.critereon;

import net.minecraft.advancements.critereon.ItemAttributeModifiersPredicate;
import net.minecraft.advancements.critereon.SingleComponentItemPredicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemAttributeModifiersPredicate.class)
public abstract class ItemAttributeModifiersPredicateInject implements SingleComponentItemPredicate<ItemAttributeModifiers> {
    @Unique
    @Override
    public boolean matches(ItemStack stack) {
        return matches(stack, stack.getAttributeModifiers());
    }
}
