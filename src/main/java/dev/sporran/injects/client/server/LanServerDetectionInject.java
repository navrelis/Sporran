/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.client.server;

import net.minecraft.client.server.LanServerDetection;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LanServerDetection.class)
public abstract class LanServerDetectionInject {
    // Sporran: We don't have any reason to implement IPv6 support, this should be handled by some other mod.
}
