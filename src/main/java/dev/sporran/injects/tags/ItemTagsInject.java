// TRACKED HASH: 19c8a1dd76d76397514e761f191accdc8254200b
package dev.sporran.injects.tags;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.helpers.mixin.CreateStatic;
import dev.sporran.injections.tags.ItemTagsInjection;

@Mixin(ItemTags.class)
public class ItemTagsInject implements ItemTagsInjection {
    @CreateStatic
    private static TagKey<Item> create(ResourceLocation name) {
        return ItemTagsInjection.create(name);
    }
}