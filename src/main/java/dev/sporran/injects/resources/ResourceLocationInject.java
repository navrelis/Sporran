// TRACKED HASH: ec6c3c6702bb8f7588d211003582f0637ede7a14
package dev.sporran.injects.resources;

import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.injections.resources.ResourceLocationInjection;

@Mixin(ResourceLocation.class)
public abstract class ResourceLocationInject implements ResourceLocationInjection {
    @Override
    public int compareNamespaced(ResourceLocation o) {
        return ResourceLocationInjection.super.compareNamespaced(o);
    }
}
