/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.server;

import net.minecraft.server.Eula;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Eula.class)
public abstract class EulaInject {
    // Sporran: don't need to implement this
}
