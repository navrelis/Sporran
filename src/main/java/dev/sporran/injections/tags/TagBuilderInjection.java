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
