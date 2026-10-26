/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.network;

import net.minecraft.network.CompressionEncoder;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CompressionEncoder.class)
public abstract class CompressionEncoderInject {
    // Sporran: doesn't seem like anything we should implement here
}
