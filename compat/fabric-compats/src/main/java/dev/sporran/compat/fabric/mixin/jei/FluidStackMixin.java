package dev.sporran.compat.fabric.mixin.jei;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import mezz.jei.api.fabric.ingredients.fluids.IJeiFluidIngredient;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import dev.sporran.util.neoforge.fluid.FluidTransferUtils;

@IfModLoaded("jei")
@Mixin(FluidStack.class)
// We use the annotation instead of traditional interface injection since the jei getAmount function collides with the FluidStack one.
// The other methods are overriden implicitly.
@Implements(value = @Interface(iface = IJeiFluidIngredient.class, prefix = "sporran$jei$"))
public abstract class FluidStackMixin {

    public @NotNull FluidVariant sporran$jei$getFluidVariant() {
        return FluidVariant.of(getFluid(), getComponentsPatch());
    }

    @Shadow
    public abstract int getAmount();

    @Shadow
    public abstract Fluid getFluid();

    @Shadow
    public abstract DataComponentPatch getComponentsPatch();

    public long sporran$jei$getAmount() {
        return FluidTransferUtils.toDroplets(getAmount());
    }

}
