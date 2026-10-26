/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.level.saveddata;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public interface SavedDataInjection {
    interface FactoryInjection {
        static <T extends SavedData> SavedData.Factory<T> create(Supplier<T> constructor, BiFunction<CompoundTag, HolderLookup.Provider, T> deserializer) {
            return new SavedData.Factory<>(constructor, deserializer, null);
        }
    }
}
