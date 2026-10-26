/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.item;

import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Items.class)
public abstract class ItemsInject {
    // Sporran: we don't need this
}
