package dev.sporran.injects.client.resources.language;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.locale.Language;
import net.neoforged.fml.i18n.I18nManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dev.sporran.injections.locale.LanguageInjection;

@Mixin(I18n.class)
public abstract class I18nInject {
    @Inject(method = "setLanguage", at = @At("TAIL"))
    private static void sporran$loadForgeLanguageData(Language language, CallbackInfo ci) {
        I18nManager.injectTranslations(language.getLanguageData());
    }
}
