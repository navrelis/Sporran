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
