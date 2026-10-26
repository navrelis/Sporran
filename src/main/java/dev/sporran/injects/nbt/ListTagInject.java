/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.nbt;

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.helpers.mixin.CreateInitializer;

import java.util.ArrayList;
import java.util.List;

@Mixin(ListTag.class)
public abstract class ListTagInject {
    ListTagInject(List<Tag> list, byte type) {}

    @CreateInitializer
    public ListTagInject(int initialCapacity) {
        this(new ArrayList<>(initialCapacity), (byte) 0);
    }
}
