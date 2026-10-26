/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.level.pathfinder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.pathfinder.PathfindingContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import dev.sporran.injections.world.level.pathfinder.PathfindingContextInjection;

@Mixin(PathfindingContext.class)
public class PathfindingContextInject implements PathfindingContextInjection {
    @Shadow
    @Final
    private BlockPos.MutableBlockPos mutablePos;

    @Override
    public BlockPos currentEvalPos() {
        return this.mutablePos;
    }
}
