/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.compat.fabric.mixin.jade_forge;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import snownee.jade.api.fluid.JadeFluidObject;
import dev.sporran.helpers.mixin.CreateStatic;

import java.util.Objects;

@Pseudo
@Mixin(value = JadeFluidObject.class, remap = false)
public abstract class JadeFluidObjectMixin {
    @CreateStatic
    private static boolean isSameFluidSameComponents(JadeFluidObject first, JadeFluidObject second) {
        if (first.getType() != second.getType()) {
            return false;
        } else {
            return first.isEmpty() && second.isEmpty() || Objects.equals(first.getComponents(), second.getComponents());
        }
    }
}
