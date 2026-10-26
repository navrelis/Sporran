// TRACKED HASH: ec6c3c6702bb8f7588d211003582f0637ede7a14
/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

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
