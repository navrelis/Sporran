/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.client.color.item;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;

import java.util.Map;

public interface ItemColorsInjection {
    Map<Item, ItemColor> sporran$getItemColors();
}
