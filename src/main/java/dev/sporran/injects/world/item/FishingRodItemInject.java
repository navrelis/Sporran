// TRACKED HASH: 62f3a843dc83d5df2702d12d71a6eaf49893cd00
/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.item;

import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = FishingRodItem.class, priority = 1010)
public class FishingRodItemInject {
    public boolean canPerformAction(ItemStack stack, ItemAbility toolAction) {
        return ItemAbilities.DEFAULT_FISHING_ROD_ACTIONS.contains(toolAction);
    }
}