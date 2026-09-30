package dev.sporran.injections.world.level.storage;

import net.minecraft.world.level.storage.PlayerDataStorage;
import dev.sporran.processor.FabricInjectedInterface;

import java.io.File;

@FabricInjectedInterface(PlayerDataStorage.class)
public interface PlayerDataStorageInjection {
    default File getPlayerDir() {
        throw new RuntimeException("mixin, why didn't you add this");
    }
}