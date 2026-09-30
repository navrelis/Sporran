package dev.sporran.injections.data.tags;

import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import dev.sporran.processor.FabricInjectedInterface;
import dev.sporran.util.SporranHelper;

@FabricInjectedInterface(TagsProvider.class)
public interface TagsProviderInjection {
    default void sporran$setModId(String modId) {
        throw new IllegalStateException();
    }

    default void sporran$setExistingFileHelper(ExistingFileHelper fileHelper) {
        throw new IllegalStateException();
    }

    default void sporran$addConstructorArgs(String modId, ExistingFileHelper fileHelper) {
        this.sporran$setModId(modId);
        this.sporran$setExistingFileHelper(fileHelper);
    }

    interface TagAppenderInjection {
        default TagBuilder getInternalBuilder() {
            throw SporranHelper.createMixinException(TagAppenderInjection.class, "getInternalBuilder");
        }

        default String getModID() {
            throw SporranHelper.createMixinException(TagAppenderInjection.class, "getModID");
        }
    }
}
