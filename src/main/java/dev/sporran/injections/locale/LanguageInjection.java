package dev.sporran.injections.locale;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import dev.sporran.util.SporranHelper;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public interface LanguageInjection {
    ThreadLocal<Map<String, Component>> sporran$componentMap = ThreadLocal.withInitial(HashMap::new);
    ThreadLocal<BiConsumer<String, Component>> sporran$componentOutput = new ThreadLocal<>();

    static void loadFromJson(InputStream stream, BiConsumer<String, String> output, BiConsumer<String, Component> componentOutput) {
        sporran$componentOutput.set(componentOutput);
        Language.loadFromJson(stream, output);
        sporran$componentOutput.remove();
    }

    default Map<String, String> getLanguageData() {
        throw SporranHelper.createMixinException(LanguageInjection.class, "getLanguageData");
    }

    default @Nullable Component getComponent(String key) {
        throw SporranHelper.createMixinException(LanguageInjection.class, "getComponent");
    }
}
