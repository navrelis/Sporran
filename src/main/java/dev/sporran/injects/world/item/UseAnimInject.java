/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.item;

import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.injections.world.item.UseAnimInjection;

import net.minecraft.world.item.UseAnim;

@Mixin(UseAnim.class)
public enum UseAnimInject implements UseAnimInjection {
    CUSTOM;
}
