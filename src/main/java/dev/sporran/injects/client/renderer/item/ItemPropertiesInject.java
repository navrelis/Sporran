// TRACKED HASH: 0ce8f1794babd31dbd3e815bf72e006d5b2176f6
package dev.sporran.injects.client.renderer.item;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.helpers.mixin.CreateStatic;
import dev.sporran.injections.client.renderer.item.ItemPropertiesInjection;

@Mixin(ItemProperties.class)
public abstract class ItemPropertiesInject {
    @CreateStatic
    private static ItemPropertyFunction registerGeneric(ResourceLocation name, ItemPropertyFunction property) {
        return ItemPropertiesInjection.registerGeneric(name, property);
    }

    @CreateStatic
    private static void register(Item item, ResourceLocation name, ItemPropertyFunction property) {
        ItemPropertiesInjection.register(item, name, property);
    }
}