// TRACKED HASH: da4c7d69266cd226e6baab431327d61c6d9516d3
/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.level.material;

import net.neoforged.neoforge.common.extensions.IFluidStateExtension;
import net.neoforged.neoforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.workarounds.FluidWorkaround;

import net.minecraft.world.level.material.FluidState;

@Implements(@Interface(iface = FluidWorkaround.class, prefix = "sporran$i$"))
@Mixin(FluidState.class)
public abstract class FluidStateInject implements IFluidStateExtension {
    @Intrinsic
    public FluidType sporran$i$getFluidType() {
        return this.neo$getFluidType();
    }
}
