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
