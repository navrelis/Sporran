/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.entity.monster;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Bogged;
import net.minecraft.world.level.Level;

@Mixin(Bogged.class)
public abstract class BoggedInject extends AbstractSkeleton {
    protected BoggedInject(EntityType<? extends AbstractSkeleton> entityType, Level level) {
        super(entityType, level);
    }

    // Sporran: shearing stuff
}
