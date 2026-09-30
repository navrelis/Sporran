// TRACKED HASH: 548c719285d4e2bcf4ee2fbb1ec72b4e0c47a89d
package dev.sporran.injects.world.level.levelgen;

import net.minecraft.world.level.levelgen.DebugLevelSource;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.helpers.mixin.CreateStatic;
import dev.sporran.injections.world.level.levelgen.DebugLevelSourceInjection;

@Mixin(DebugLevelSource.class)
public class DebugLevelSourceInject implements DebugLevelSourceInjection {
    @CreateStatic
    private static void initValidStates() {
        DebugLevelSourceInjection.initValidStates();
    }
}