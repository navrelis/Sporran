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
