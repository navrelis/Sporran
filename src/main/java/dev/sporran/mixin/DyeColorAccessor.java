/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.mixin;

import net.minecraft.world.item.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.IntFunction;

@Mixin(DyeColor.class)
public interface DyeColorAccessor {
    @Accessor("BY_ID")
    static IntFunction<DyeColor> getById() {
        throw new UnsupportedOperationException();
    }
}
