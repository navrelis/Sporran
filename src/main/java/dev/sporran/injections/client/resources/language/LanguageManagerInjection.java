/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

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
