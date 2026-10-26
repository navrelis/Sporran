/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.client.particle;

import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.injections.client.particle.ParticleRenderTypeInjection;

import net.minecraft.client.particle.ParticleRenderType;

@Mixin(ParticleRenderType.class)
public interface ParticleRenderTypeInject extends ParticleRenderTypeInjection {
    @Mixin(targets = "net.minecraft.client.particle.ParticleRenderType$2")
    abstract class ParticleSheetOpaqueInject implements ParticleRenderTypeInjection {
        @Override
        public boolean isTranslucent() {
            return false;
        }
    }

    @Mixin(targets = "net.minecraft.client.particle.ParticleRenderType$4")
    abstract class ParticleSheetLitInject implements ParticleRenderTypeInjection {
        @Override
        public boolean isTranslucent() {
            return false;
        }
    }

    @Override
    default boolean isTranslucent() {
        return true;
    }
}
