/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.server.commands;

import java.util.Collection;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.server.commands.DataPackCommand;
import net.minecraft.server.packs.repository.Pack;

@Mixin(DataPackCommand.class)
public abstract class DataPackCommandInject {
    @ModifyExpressionValue(method = "listEnabledPacks", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/repository/PackRepository;getSelectedPacks()Ljava/util/Collection;"))
    private static Collection<Pack> sporran$filterHiddenPacks(Collection<Pack> original) {
        return original.stream().filter(p -> !p.isHidden()).toList();
    }
}
