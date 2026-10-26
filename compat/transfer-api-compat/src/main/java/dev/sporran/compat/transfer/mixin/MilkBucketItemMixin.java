/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.compat.transfer.mixin;

import net.fabricmc.fabric.mixin.transfer.BucketItemAccessor;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MilkBucketItem.class)
public abstract class MilkBucketItemMixin implements BucketItemAccessor {
    @Override
    public Fluid fabric_getFluid() {
        return NeoForgeMod.MILK.asOptional().orElse(null);
    }
}
