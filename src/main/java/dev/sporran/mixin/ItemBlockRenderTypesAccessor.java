/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.mixin;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(ItemBlockRenderTypes.class)
public interface ItemBlockRenderTypesAccessor {
    @Accessor
    static boolean isRenderCutout() {
        throw new UnsupportedOperationException();
    }

    @Accessor("TYPE_BY_BLOCK")
    static Map<Block, RenderType> getTypeByBlock() {
        throw new UnsupportedOperationException();
    }

    @Accessor("TYPE_BY_FLUID")
    static Map<Fluid, RenderType> getTypeByFluid() {
        throw new UnsupportedOperationException();
    }
}
