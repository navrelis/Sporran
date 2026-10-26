/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.compat.fabric.mixin.sable;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import dev.ryanhcode.sable.api.block.propeller.BlockEntityPropeller;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.helpers.mixin.AbstractOverride;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

@IfModLoaded("sable")
@Mixin(BlockEntityPropeller.class)
public interface BlockEntityPropellerMixin {
    // Sporran TODO: If Sable solves this in a future update, remove this.

    @AbstractOverride
    default Level getLevel() {
        if (this instanceof BlockEntity blockEntity) {
            return blockEntity.getLevel();
        }

        return null;
    }

    @AbstractOverride
    default BlockPos getBlockPos() {
        if (this instanceof BlockEntity blockEntity) {
            return blockEntity.getBlockPos();
        }

        return null;
    }
}
