/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.tags;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;
import dev.sporran.processor.FabricInjectedInterface;
import dev.sporran.util.SporranHelper;

@FabricInjectedInterface(TagEntry.class)
public interface TagEntryInjection {
    default ResourceLocation getId() {
        throw SporranHelper.createMixinException(TagEntry.class, "getId");
    }

    default boolean isRequired() {
        throw SporranHelper.createMixinException(TagEntry.class, "isRequired");
    }

    default boolean isTag() {
        throw SporranHelper.createMixinException(TagEntry.class, "isTag");
    }
}
