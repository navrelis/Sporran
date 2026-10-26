/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.entity.monster;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.injections.world.entity.monster.SlimeInjection;

@Mixin(Slime.class)
public abstract class SlimeInject extends Mob implements SlimeInjection {
    protected SlimeInject(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    // Sporran: handled by Porting Lib, actually

    @Override
    public boolean spawnCustomParticles() {
        return false;
    }
}
