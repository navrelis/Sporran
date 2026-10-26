/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.data;

import net.minecraft.data.Main;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Main.class)
public abstract class MainInject {
    // Sporran: we shouldn't have to mess with this honestly
}
