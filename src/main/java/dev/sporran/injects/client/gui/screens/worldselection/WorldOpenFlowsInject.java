// TRACKED HASH: 23b22600ecc8455bd8096ea262c743414a793541
/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.client.gui.screens.worldselection;

import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WorldOpenFlows.class)
public abstract class WorldOpenFlowsInject {
    // Sporran: doesn't look like this is very substantial of a patch?
}