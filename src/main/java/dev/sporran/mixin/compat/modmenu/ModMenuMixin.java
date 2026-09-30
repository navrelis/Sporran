package dev.sporran.mixin.compat.modmenu;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import com.terraformersmc.modmenu.ModMenu;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import dev.sporran.loader.SporranLoader;

@IfModLoaded("modmenu")
@Pseudo
@Mixin(ModMenu.class)
public abstract class ModMenuMixin {
    @Inject(method = "getConfigScreenFactory", at = @At("HEAD"), cancellable = true)
    private static void sporran$addForgeConfigScreenIfPossible(String modId, CallbackInfoReturnable<ConfigScreenFactory<?>> cir) {
        var sporranMod = SporranLoader.Companion.getInstance().getMod(modId);
        if (sporranMod != null) {
            // Sporran: Add Forge config screens to ModMenu
            var container = sporranMod.getContainer();
            var screenExtension = container.getCustomExtension(IConfigScreenFactory.class);

            screenExtension.ifPresent(iConfigScreenFactory ->
                cir.setReturnValue(parent -> iConfigScreenFactory.createScreen(container, parent))
            );
        }
    }
}
