/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.commands;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.resources.ResourceLocation;

@Mixin(SharedSuggestionProvider.class)
public interface SharedSuggestionProviderInject {
    @Definition(id = "resourceLocation", local = @Local(type = ResourceLocation.class))
    @Definition(id = "getNamespace", method = "Lnet/minecraft/resources/ResourceLocation;getNamespace()Ljava/lang/String;")
    @Definition(id = "equals", method = "Ljava/lang/String;equals(Ljava/lang/Object;)Z")
    @Expression("resourceLocation.getNamespace().equals('minecraft')")
    @ModifyExpressionValue(method = "filterResources(Ljava/lang/Iterable;Ljava/lang/String;Ljava/util/function/Function;Ljava/util/function/Consumer;)V", at = @At("MIXINEXTRAS:EXPRESSION"))
    private static boolean sporran$alwaysAllowModdedSuggestions(boolean original) {
        return true;
    }
}
