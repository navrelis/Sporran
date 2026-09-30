package dev.sporran.injections.core.particles;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import dev.sporran.processor.FabricInjectedInterface;
import dev.sporran.util.SporranHelper;

@FabricInjectedInterface(BlockParticleOption.class)
public interface BlockParticleOptionInjection {
    default BlockParticleOption setPos(BlockPos pos) {
        throw SporranHelper.createMixinException(BlockParticleOptionInjection.class, "setPos");
    }

    default BlockPos getPos() {
        throw SporranHelper.createMixinException(BlockParticleOptionInjection.class, "getPos");
    }
}
