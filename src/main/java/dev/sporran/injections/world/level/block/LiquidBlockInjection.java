package dev.sporran.injections.world.level.block;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import dev.sporran.util.SporranHelper;

import java.util.function.Supplier;

public interface LiquidBlockInjection {
    static LiquidBlock create(Supplier<? extends FlowingFluid> fluidSupplier, BlockBehaviour.Properties properties) {
        return new LiquidBlock(fluidSupplier.get(), properties);
    }

    default FlowingFluid getFluid() {
        throw SporranHelper.createMixinException(LiquidBlockInjection.class, "getFluid");
    }
}
