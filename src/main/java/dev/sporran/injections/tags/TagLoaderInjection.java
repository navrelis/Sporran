/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.tags;

import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagLoader;
import dev.sporran.processor.FabricInjectedInterface;
import dev.sporran.util.SporranHelper;

public interface TagLoaderInjection {
    @FabricInjectedInterface(TagLoader.EntryWithSource.class)
    public interface EntryWithSourceInjection {
        static TagLoader.EntryWithSource create(TagEntry entry, String source, boolean remove) {
            var entryWithSource = new TagLoader.EntryWithSource(entry, source);
            ((EntryWithSourceInjection) (Object) entryWithSource).sporran$setRemove(remove);

            return entryWithSource;
        }

        default boolean remove() {
            throw SporranHelper.createMixinException(TagLoader.EntryWithSource.class, "remove");
        }

        default void sporran$setRemove(boolean remove) {
            throw SporranHelper.createMixinException(TagLoader.EntryWithSource.class, "sporran$setRemove");
        }
    }
}
