/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.tags;

import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagEntry;
import dev.sporran.processor.FabricInjectedInterface;
import dev.sporran.util.SporranHelper;

import java.util.stream.Stream;

@FabricInjectedInterface(TagBuilder.class)
public interface TagBuilderInjection {
    default TagBuilder remove(final TagEntry entry) {
        throw new IllegalStateException();
    }

    default Stream<TagEntry> getRemoveEntries() {
        throw new IllegalStateException();
    }

    default TagBuilder replace(boolean value) {
        throw new IllegalStateException();
    }

    default TagBuilder replace() {
        return replace(true);
    }

    default boolean isReplace() {
        throw SporranHelper.createMixinException(TagBuilderInjection.class, "isReplace");
    }
}
