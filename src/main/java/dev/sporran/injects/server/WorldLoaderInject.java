// TRACKED HASH: 73e5f001c405920db0c51467eaae6d0f5e817e2b
/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.server;

import net.minecraft.server.WorldLoader;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WorldLoader.class)
public class WorldLoaderInject {
    /**
     * This is already handled by Fabric API.
     */
}