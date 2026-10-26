package dev.sporran.mixin.compat.neoforgedatapackextensions;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dev.sporran.helpers.ForeignDataMapTypes;

import java.util.Map;

/**
 * Sporran: adds the data map types registered through NeoForge Data Pack Extensions (see {@link ForeignDataMapTypes}).
 */
@IfModLoaded("neoforgedatapackextensions")
@Mixin(value = RegisterDataMapTypesEvent.class, remap = false)
public abstract class RegisterDataMapTypesEventMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void sporran$registerForeignDataMapTypes(Map<?, ?> attachments, CallbackInfo ci) {
        ForeignDataMapTypes.registerAll((RegisterDataMapTypesEvent) (Object) this);
    }
}
