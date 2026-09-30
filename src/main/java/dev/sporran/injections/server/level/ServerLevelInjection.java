package dev.sporran.injections.server.level;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.capabilities.ICapabilityInvalidationListener;
import net.neoforged.neoforge.entity.PartEntity;
import dev.sporran.injections.world.level.LevelInjection;
import dev.sporran.util.SporranHelper;

public interface ServerLevelInjection extends LevelInjection {
    default Int2ObjectMap<PartEntity<?>> sporran$getEntityParts() {
        throw SporranHelper.createMixinException(ServerLevelInjection.class, "sporran$getEntityParts");
    }

    default void registerCapabilityListener(BlockPos pos, ICapabilityInvalidationListener listener) {
        throw SporranHelper.createMixinException(ServerLevelInjection.class, "registerCapabilityListener");
    }

    default void cleanCapabilityListenerReferences() {
        throw SporranHelper.createMixinException(ServerLevelInjection.class, "cleanCapabilityListenerReferences");
    }

    @Override
    default void setDayTimeFraction(float dayTimeFraction) {
        throw SporranHelper.createMixinException(ServerLevelInjection.class, "setDayTimeFraction");
    }

    @Override
    default float getDayTimeFraction() {
        throw SporranHelper.createMixinException(ServerLevelInjection.class, "getDayTimeFraction");
    }

    @Override
    default float getDayTimePerTick() {
        throw SporranHelper.createMixinException(ServerLevelInjection.class, "getDayTimePerTick");
    }
}
