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
