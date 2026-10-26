/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.server.commands;

import java.util.Collection;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.neoforged.neoforge.resource.ResourcePackLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.server.commands.ReloadCommand;
import net.minecraft.server.packs.repository.PackRepository;

@Mixin(ReloadCommand.class)
public abstract class ReloadCommandInject {
    @ModifyReturnValue(method = "discoverNewPacks", at = @At("RETURN"))
    private static Collection<String> sporran$reorderNewPacks(Collection<String> original, @Local(argsOnly = true) Collection<String> old, @Local(argsOnly = true) PackRepository repository) {
        ResourcePackLoader.reorderNewlyDiscoveredPacks(original, old, repository);
        return original;
    }
}
