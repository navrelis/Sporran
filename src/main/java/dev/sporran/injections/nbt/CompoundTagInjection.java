package dev.sporran.injections.nbt;

import net.minecraft.nbt.CompoundTag;
import dev.sporran.mixin.CompoundTagAccessor;

import java.util.HashMap;

public interface CompoundTagInjection {
    static CompoundTag create(int expectedEntries) {
        return CompoundTagAccessor.createCompoundTag(HashMap.newHashMap(expectedEntries));
    }
}
