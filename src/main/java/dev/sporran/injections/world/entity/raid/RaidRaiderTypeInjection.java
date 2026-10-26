/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

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
