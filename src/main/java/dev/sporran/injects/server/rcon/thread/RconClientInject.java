/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.server.rcon.thread;

import net.minecraft.server.rcon.thread.RconClient;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RconClient.class)
public abstract class RconClientInject {
    // Sporran: I'm not touching this
}
