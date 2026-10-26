/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.level.block;

import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import net.minecraft.world.item.ItemStack;

public interface ComposterBlockInjection {
    static float getValue(ItemStack item) {
        var value = item.getItemHolder().getData(NeoForgeDataMaps.COMPOSTABLES);
        if (value != null)
            return value.chance();

        return -1f;
    }
}
