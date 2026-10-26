/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.nbt;

import net.minecraft.nbt.CompoundTag;
import dev.sporran.mixin.CompoundTagAccessor;

import java.util.HashMap;

public interface CompoundTagInjection {
    static CompoundTag create(int expectedEntries) {
        return CompoundTagAccessor.createCompoundTag(HashMap.newHashMap(expectedEntries));
    }
}
