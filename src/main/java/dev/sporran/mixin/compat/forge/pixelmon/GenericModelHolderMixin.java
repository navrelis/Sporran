/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.mixin.compat.forge.pixelmon;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

import java.lang.reflect.Constructor;
import java.util.Arrays;

@Pseudo
@Mixin(targets = "com.pixelmonmod.pixelmon.client.render.GenericModelHolder")
public abstract class GenericModelHolderMixin {
    // why would you ever willingly make your own mod code reflection-based. especially in rendering. that sounds like a performance nightmare.
    @ModifyExpressionValue(method = "loadModel", at = @At(value = "INVOKE", target = "Ljava/lang/Class;getConstructors()[Ljava/lang/reflect/Constructor;"))
    private Constructor<?>[] sporran$pixelmon$getConstructorWithParams(Constructor<?>[] original) {
        return Arrays.stream(original).filter(e -> e.getParameterCount() > 0).toList().toArray(new Constructor[0]);
    }
}
