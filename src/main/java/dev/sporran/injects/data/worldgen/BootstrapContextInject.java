// TRACKED HASH: 50ccda2541c46785a0be46cf552b476164633abf
/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.data.worldgen;

import net.minecraft.data.worldgen.BootstrapContext;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.injections.data.worldgen.BootstrapContextInjection;

@Mixin(BootstrapContext.class)
public interface BootstrapContextInject extends BootstrapContextInjection {
}