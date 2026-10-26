/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.level.dimension.end;

import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import dev.sporran.injections.world.level.dimension.end.EndDragonFightInjection;

@Mixin(EndDragonFight.class)
public abstract class EndDragonFightInject implements EndDragonFightInjection {
    @Shadow @Final private ServerBossEvent dragonEvent;

    @Override
    public void addPlayer(ServerPlayer player) {
        this.dragonEvent.addPlayer(player);
    }

    @Override
    public void removePlayer(ServerPlayer player) {
        this.dragonEvent.removePlayer(player);
    }
}
