/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.tags;

import java.util.List;

import dev.sporran.processor.FabricInjectedInterface;
import dev.sporran.util.SporranHelper;

import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagFile;

import net.fabricmc.fabric.api.tag.v1.FabricTagFile;

@FabricInjectedInterface(TagFile.class)
public interface TagFileInjection extends FabricTagFile {
    default List<TagEntry> remove() {
        throw SporranHelper.createMixinException(TagFile.class, "remove");
    }

    default void sporran$setRemove(List<TagEntry> remove) {
        throw SporranHelper.createMixinException(TagFile.class, "sporran$setRemove");
    }
}
