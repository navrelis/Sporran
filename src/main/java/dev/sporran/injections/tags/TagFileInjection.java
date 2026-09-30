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
