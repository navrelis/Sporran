/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.client.multiplayer;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.multiplayer.KnownPacksManager;

@Mixin(KnownPacksManager.class)
public abstract class KnownPacksManagerInject {
    // Sporran: don't think we need to do anything about this?
}
