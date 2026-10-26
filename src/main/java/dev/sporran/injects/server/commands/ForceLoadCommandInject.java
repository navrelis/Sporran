/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.server.commands;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.server.commands.ForceLoadCommand;

@Mixin(ForceLoadCommand.class)
public abstract class ForceLoadCommandInject {
    // Sporran TODO: do we add support for this
}
