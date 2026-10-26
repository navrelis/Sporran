/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.core.component;

import net.minecraft.core.component.PatchedDataComponentMap;
import dev.sporran.processor.FabricInjectedInterface;
import dev.sporran.util.SporranHelper;

@FabricInjectedInterface(PatchedDataComponentMap.class)
public interface PatchedDataComponentMapInjection {
    default boolean isPatchEmpty() {
        throw SporranHelper.createMixinException(PatchedDataComponentMapInjection.class, "isPatchEmpty");
    }
}
