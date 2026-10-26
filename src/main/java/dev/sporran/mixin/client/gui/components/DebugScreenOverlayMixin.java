/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.mixin.client.gui.components;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import dev.sporran.Sporran;
import dev.sporran.loader.asm.coremod.CoreModLoader;

import net.minecraft.client.gui.components.DebugScreenOverlay;

import net.fabricmc.loader.api.FabricLoader;

@Mixin(DebugScreenOverlay.class)
public class DebugScreenOverlayMixin {
    @Inject(at = @At("RETURN"), method = "getSystemInformation")
    public void sporran$appendModInfo(CallbackInfoReturnable<List<String>> cir) {
        var messages = cir.getReturnValue();

        messages.add("");

        var version = FabricLoader.getInstance().getModContainer(Sporran.MOD_ID).orElseThrow().getMetadata().getVersion().getFriendlyString();
        var color = "§";

        if (version.contains("-local"))
            color += "c";
        else if (version.contains("-nightly"))
            color += "6";
        else
            color += "b";

        messages.add("Sporran " + color + "v" + version);
        messages.add(Sporran.Companion.getLoader().getMods().size() + " mods loaded");

        if (CoreModLoader.INSTANCE.getEnableCoreMods())
            messages.add(CoreModLoader.INSTANCE.getLoadedCoreMods() + " coremods loaded");

        messages.add("");
    }
}
