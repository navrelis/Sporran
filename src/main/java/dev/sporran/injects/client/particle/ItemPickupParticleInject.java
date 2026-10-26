/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.client.particle;

import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.injections.world.phys.AABBInjection;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ItemPickupParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.phys.AABB;

@Mixin(ItemPickupParticle.class)
public abstract class ItemPickupParticleInject extends Particle {
    protected ItemPickupParticleInject(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    @Override
    public AABB getRenderBoundingBox(float partialTicks) {
        return AABBInjection.INFINITE;
    }
}
