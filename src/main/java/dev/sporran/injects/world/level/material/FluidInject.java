// TRACKED HASH: 8052166e9529780ad90ee8b00eda7d0ee8ffc2ca
package dev.sporran.injects.world.level.material;

import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.extensions.IFluidExtension;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import dev.sporran.util.SporranHelper;
import dev.sporran.workarounds.FluidWorkaround;

import net.minecraft.world.level.material.Fluid;

@Implements(@Interface(iface = FluidWorkaround.class, prefix = "sporran$i$"))
@Mixin(Fluid.class)
public abstract class FluidInject implements IFluidExtension, FluidWorkaround {
    private FluidType forgeFluidType;

    // Sporran: called for every entity every tick (and by pathfinding), resolve the override check only once.
    @Unique private static final SporranHelper.MethodOverrideCheck sporran$GET_FLUID_TYPE_OVERRIDE = SporranHelper.MethodOverrideCheck.withReturnType(Fluid.class, "getFluidType", FluidType.class);

    @NotNull
    @Override
    public FluidType neo$getFluidType() {
        // Sporran: We pray that this works
        if (sporran$GET_FLUID_TYPE_OVERRIDE.test(this.getClass())) {
            return this.getFluidType();
        }

        if (forgeFluidType == null)
            forgeFluidType = CommonHooks.getVanillaFluidType((Fluid) (Object) this);

        return forgeFluidType;
    }

    @Intrinsic
    public FluidType sporran$i$getFluidType() {
        return this.neo$getFluidType();
    }
}
