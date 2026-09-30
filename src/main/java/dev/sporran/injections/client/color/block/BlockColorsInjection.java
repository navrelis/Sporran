package dev.sporran.injections.client.color.block;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public interface BlockColorsInjection {
    Map<Block, BlockColor> sporran$getBlockColors();
}
