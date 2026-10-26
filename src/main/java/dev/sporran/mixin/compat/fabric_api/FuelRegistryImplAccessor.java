/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.mixin.compat.fabric_api;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.fabricmc.fabric.impl.content.registry.FuelRegistryImpl;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FuelRegistryImpl.class)
public interface FuelRegistryImplAccessor {
    @Accessor
    Object2IntMap<ItemLike> getItemCookTimes();

    @Accessor
    Object2IntMap<TagKey<Item>> getTagCookTimes();
}
