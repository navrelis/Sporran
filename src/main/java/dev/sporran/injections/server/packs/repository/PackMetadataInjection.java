/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.server.packs.repository;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.world.flag.FeatureFlagSet;
import dev.sporran.util.SporranHelper;

import java.util.List;

public interface PackMetadataInjection {
    static Pack.Metadata create(Component description, PackCompatibility compatibility, FeatureFlagSet requestedFeatures, List<String> overlays, boolean hidden) {
        var metadata = new Pack.Metadata(description, compatibility, requestedFeatures, overlays);
        metadata.sporran$markForge();
        metadata.sporran$setHidden(hidden);
        return metadata;
    }

    default void sporran$setHidden(boolean hidden) {
        throw SporranHelper.createMixinException(PackMetadataInjection.class, "sporran$setHidden");
    }

    default void sporran$markForge() {
        throw SporranHelper.createMixinException(PackMetadataInjection.class, "sporran$markForge");
    }

    default boolean hidden() {
        throw SporranHelper.createMixinException(PackMetadataInjection.class, "hidden");
    }
}
