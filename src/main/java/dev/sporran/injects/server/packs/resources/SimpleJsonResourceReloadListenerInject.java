// TRACKED HASH: b77abfa3d010ae58c80d911cff64c39f34dadbd6
/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.server.packs.resources;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import dev.sporran.injections.server.packs.resources.SimpleJsonResourceReloadListenerInjection;

@Mixin(SimpleJsonResourceReloadListener.class)
public class SimpleJsonResourceReloadListenerInject implements SimpleJsonResourceReloadListenerInjection {
    @Shadow @Final private String directory;

    public ResourceLocation getPreparedPath(ResourceLocation rl) {
        return rl.withPath(this.directory + "/" + rl.getPath() + ".json");
    }
}
