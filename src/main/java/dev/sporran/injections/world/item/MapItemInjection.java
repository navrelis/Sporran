/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import dev.sporran.util.SporranHelper;

public interface MapItemInjection {
    default MapItemSavedData getCustomMapData(ItemStack stack, Level level) {
        throw SporranHelper.createMixinException(MapItemInjection.class, "getCustomMapData");
    }
}
