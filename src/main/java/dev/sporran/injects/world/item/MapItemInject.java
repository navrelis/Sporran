package dev.sporran.injects.world.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import dev.sporran.injections.world.item.MapItemInjection;
import dev.sporran.util.SporranHelper;

@Mixin(MapItem.class)
public abstract class MapItemInject implements MapItemInjection {
    @Shadow
    @Nullable
    public static MapItemSavedData getSavedData(@Nullable MapId mapId, Level level) {
        throw new IllegalStateException();
    }

    @Inject(method = "getSavedData(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;", at = @At("HEAD"), cancellable = true)
    private static void sporran$tryUseCustomMapData(ItemStack stack, Level level, CallbackInfoReturnable<MapItemSavedData> cir) {
        var item = stack.getItem();

        if (!(item instanceof MapItem mapItem))
            return;

        if (SporranHelper.INSTANCE.hasMethodOverride(mapItem.getClass(), MapItem.class, "getCustomMapData", ItemStack.class, Level.class)) {
            cir.setReturnValue(mapItem.getCustomMapData(stack, level));
        }
    }

    @Override
    public MapItemSavedData getCustomMapData(ItemStack stack, Level level) {
        var id = stack.get(DataComponents.MAP_ID);
        return getSavedData(id, level);
    }
}
