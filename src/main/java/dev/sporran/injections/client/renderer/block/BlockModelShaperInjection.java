/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.client.renderer.block;

import dev.sporran.util.SporranHelper;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockModelShaperInjection {
    default TextureAtlasSprite getTexture(BlockState state, Level level, BlockPos pos) {
        throw SporranHelper.createMixinException(BlockModelShaperInjection.class, "getTexture");
    }
}
