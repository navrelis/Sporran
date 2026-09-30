package dev.sporran.injections.client.resources.language;

import net.minecraft.client.resources.language.LanguageManager;
import dev.sporran.processor.FabricInjectedInterface;
import dev.sporran.util.SporranHelper;

import java.util.Locale;

@FabricInjectedInterface(LanguageManager.class)
public interface LanguageManagerInjection {
    default Locale getJavaLocale() {
        throw SporranHelper.createMixinException(LanguageManagerInjection.class, "getJavaLocale");
    }
}
