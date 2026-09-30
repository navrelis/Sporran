package dev.sporran.mixin.compat.modmenu;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import com.terraformersmc.modmenu.util.mod.fabric.FabricIconHandler;
import net.fabricmc.loader.api.ModContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import dev.sporran.loader.SporranLoader;

// Sporran: ModMenu is an optional (client-side) mod, so skip this cleanly when it isn't installed
// instead of logging a "@Mixin target was not found" warning on every startup.
@IfModLoaded("modmenu")
@Pseudo
@Mixin(FabricIconHandler.class)
public abstract class FabricIconHandlerMixin {
    @WrapWithCondition(method = "createIcon", at = @At(value = "INVOKE", target = "Lorg/apache/commons/lang3/Validate;validState(ZLjava/lang/String;[Ljava/lang/Object;)V", remap = false))
    private static boolean sporran$useIconAnywayIfForge(boolean expression, String message, Object[] values, @Local(argsOnly = true) ModContainer iconSource) {
        return !SporranLoader.Companion.getInstance().hasMod(iconSource.getMetadata().getId());
    }
}
