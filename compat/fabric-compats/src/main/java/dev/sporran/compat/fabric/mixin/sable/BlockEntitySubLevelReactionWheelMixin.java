package dev.sporran.compat.fabric.mixin.sable;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import dev.ryanhcode.sable.api.block.BlockEntitySubLevelReactionWheel;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.helpers.mixin.AbstractOverride;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

@IfModLoaded("sable")
@Mixin(BlockEntitySubLevelReactionWheel.class)
public interface BlockEntitySubLevelReactionWheelMixin {
    // Sporran TODO: If Sable solves this in a future update, remove this.
    
    @AbstractOverride
    default BlockState getBlockState() {
        if (this instanceof BlockEntity blockEntity) {
            return blockEntity.getBlockState();
        }

        return null;
    }
}
