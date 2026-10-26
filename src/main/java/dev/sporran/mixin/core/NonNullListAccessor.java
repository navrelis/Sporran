/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.mixin.core;

import net.minecraft.core.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(NonNullList.class)
public interface NonNullListAccessor {
    @Invoker("<init>")
    static <E> NonNullList<E> createNonNullList(List<E> list, E defaultValue) {
        throw new IllegalStateException();
    }
}
