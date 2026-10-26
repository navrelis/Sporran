/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.level.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedStoneOreBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RedStoneOreBlock.class)
public abstract class RedStoneOreBlockInject extends Block {
    public RedStoneOreBlockInject(Properties properties) {
        super(properties);
    }

    // Sporran: handled by Porting Lib
}
