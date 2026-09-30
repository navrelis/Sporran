package dev.sporran.injections.world.entity.raid;

import java.util.function.Supplier;

import dev.sporran.util.SporranHelper;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.raid.Raider;

public interface RaidRaiderTypeInjection {

    default Supplier<EntityType<? extends Raider>> sporran$getEntityTypeSupplier() {
        throw SporranHelper.createMixinException(RaidRaiderTypeInjection.class, "sporran$getEntityTypeSupplier");
    }

    default boolean sporran$isNeo() {
        throw SporranHelper.createMixinException(RaidRaiderTypeInjection.class, "sporran$isNeo");
    }
}
