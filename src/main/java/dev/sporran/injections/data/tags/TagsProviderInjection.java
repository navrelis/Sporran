/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

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
