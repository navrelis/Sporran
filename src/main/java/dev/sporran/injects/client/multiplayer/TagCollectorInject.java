/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.client.multiplayer;

import net.minecraft.client.multiplayer.TagCollector;
import net.minecraft.core.RegistryAccess;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TagCollector.class)
public abstract class TagCollectorInject {
    @Inject(method = "updateTags", at = @At("TAIL"))
    private void sporran$callTagsUpdatedEvent(RegistryAccess registryAccess, boolean isMemoryConnection, CallbackInfo ci) {
        NeoForge.EVENT_BUS.post(new TagsUpdatedEvent(registryAccess, true, isMemoryConnection));
    }
}
