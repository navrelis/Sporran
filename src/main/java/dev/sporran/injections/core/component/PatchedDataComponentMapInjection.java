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
