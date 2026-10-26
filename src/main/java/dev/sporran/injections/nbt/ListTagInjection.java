/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.nbt;

import net.minecraft.nbt.ListTag;
import dev.sporran.mixin.ListTagAccessor;

import java.util.ArrayList;

public interface ListTagInjection {
    static ListTag create(int initialCapacity) {
        return ListTagAccessor.createListTag(new ArrayList<>(initialCapacity), (byte) 0);
    }
}
