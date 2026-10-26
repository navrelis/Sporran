// TRACKED HASH: 0db15f8493ae5ebcaa5b0a0a9f76b6836a06aa9c
/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.commands.arguments.selector;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.neoforged.neoforge.common.command.EntitySelectorManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntitySelectorParser.class)
public abstract class EntitySelectorParserInject {
    @Inject(method = "parse", at = @At(value = "INVOKE", target = "Lnet/minecraft/commands/arguments/selector/EntitySelectorParser;parseSelector()V"), cancellable = true)
    private void sporran$tryUseForgeSelector(CallbackInfoReturnable<EntitySelector> cir) throws CommandSyntaxException {
        var forgeSelector = EntitySelectorManager.parseSelector((EntitySelectorParser) (Object) this);
        if (forgeSelector != null)
            cir.setReturnValue(forgeSelector);
    }

    @Inject(method = "fillSelectorSuggestions", at = @At("TAIL"))
    private static void sporran$fillForgeSelectorSuggestions(SuggestionsBuilder builder, CallbackInfo ci) {
        EntitySelectorManager.fillSelectorSuggestions(builder);
    }
}