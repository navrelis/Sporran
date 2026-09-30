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
