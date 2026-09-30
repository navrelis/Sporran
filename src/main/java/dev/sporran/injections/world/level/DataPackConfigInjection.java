package dev.sporran.injections.world.level;

import net.minecraft.world.level.DataPackConfig;
import dev.sporran.processor.FabricInjectedInterface;

import java.util.List;

@FabricInjectedInterface(DataPackConfig.class)
public interface DataPackConfigInjection {
    default void addModPacks(List<String> modPacks) {
        throw new IllegalStateException();
    }
}
