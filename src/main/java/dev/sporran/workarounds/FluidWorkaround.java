package dev.sporran.workarounds;

import net.neoforged.neoforge.fluids.FluidType;
import dev.sporran.util.SporranHelper;

public interface FluidWorkaround {
    default FluidType getFluidType() {
        throw SporranHelper.createMixinException(FluidWorkaround.class, "getFluidType");
    }
}
