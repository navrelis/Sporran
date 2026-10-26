/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.server.network.config;

import net.minecraft.server.network.config.SynchronizeRegistriesTask;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SynchronizeRegistriesTask.class)
public abstract class SynchronizeRegistriesTaskInject {
    // Sporran: don't think we need this?
}
