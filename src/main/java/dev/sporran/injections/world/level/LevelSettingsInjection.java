package dev.sporran.injections.world.level;

import com.mojang.serialization.Lifecycle;
import net.minecraft.world.level.LevelSettings;
import dev.sporran.util.SporranHelper;

public interface LevelSettingsInjection {
    default LevelSettings withLifecycle(Lifecycle lifecycle) {
        throw SporranHelper.createMixinException(LevelSettingsInjection.class, "withLifecycle");
    }

    default Lifecycle getLifecycle() {
        throw SporranHelper.createMixinException(LevelSettingsInjection.class, "getLifecycle");
    }

    default void sporran$setLifecycle(Lifecycle lifecycle) {
        throw SporranHelper.createMixinException(LevelSettingsInjection.class, "sporran$setLifecycle");
    }
}
