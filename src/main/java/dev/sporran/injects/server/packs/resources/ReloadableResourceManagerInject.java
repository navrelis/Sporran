// TRACKED HASH: fcbd779256061257dc96dee80bbb7cb48e719c79
/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.server.packs.resources;

import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ReloadableResourceManager.class)
public abstract class ReloadableResourceManagerInject {
    @Shadow @Final private List<PreparableReloadListener> listeners;

    @Shadow public abstract void registerReloadListener(PreparableReloadListener listener);

    public void registerReloadListenerIfNotPresent(PreparableReloadListener listener) {
        if (!this.listeners.contains(listener)) {
            this.registerReloadListener(listener);
        }
    }
}