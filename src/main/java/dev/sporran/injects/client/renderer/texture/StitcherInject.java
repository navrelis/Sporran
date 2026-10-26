// TRACKED HASH: 0b3087cc25c2d2fd27ea17dbdbb73d5d1d3d7c79
/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.client.renderer.texture;

import net.minecraft.client.renderer.texture.Stitcher;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Stitcher.class)
public abstract class StitcherInject {
    // TODO: is there much point for Sporran to implement this?
}