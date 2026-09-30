// TRACKED HASH: 834fc3d6b652f61039af4bb7ec29cdea5fe3bc0e
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