/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

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
