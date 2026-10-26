/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.data.advancements;

import net.minecraft.data.advancements.AdvancementProvider;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AdvancementProvider.class)
public abstract class AdvancementProviderInject {
    // Sporran: all this does is adds Deprecated.
    //       let's just bump the patch count.
}
