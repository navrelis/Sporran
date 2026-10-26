// TRACKED HASH: 834fc3d6b652f61039af4bb7ec29cdea5fe3bc0e
/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.tags;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.helpers.mixin.CreateStatic;
import dev.sporran.injections.tags.BlockTagsInjection;

@Mixin(BlockTags.class)
public class BlockTagsInject implements BlockTagsInjection {
    @CreateStatic
    private static TagKey<Block> create(ResourceLocation name) {
        return BlockTagsInjection.create(name);
    }
}