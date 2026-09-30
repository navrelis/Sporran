package dev.sporran.injections.world.level.pathfinder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.pathfinder.PathfindingContext;
import dev.sporran.processor.FabricInjectedInterface;
import dev.sporran.util.SporranHelper;

@FabricInjectedInterface(PathfindingContext.class)
public interface PathfindingContextInjection {
    default BlockPos currentEvalPos() {
        throw SporranHelper.createMixinException(PathfindingContextInjection.class, "currentEvalPos");
    }
}
