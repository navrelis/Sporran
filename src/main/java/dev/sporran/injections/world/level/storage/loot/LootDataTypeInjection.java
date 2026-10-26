/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.level.storage.loot;

import java.util.Optional;
import java.util.function.BiConsumer;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.Nullable;
import dev.sporran.util.SporranHelper;

import net.minecraft.resources.ResourceLocation;

public interface LootDataTypeInjection<T> {
    default @Nullable T defaultValue() {
        throw SporranHelper.createMixinException(LootDataTypeInjection.class, "defaultValue");
    }

    default Codec<Optional<T>> conditionalCodec() {
        throw SporranHelper.createMixinException(LootDataTypeInjection.class, "conditionalCodec");
    }

    default BiConsumer<T, ResourceLocation> idSetter() {
        throw SporranHelper.createMixinException(LootDataTypeInjection.class, "idSetter");
    }
}
