/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.mixin.compat.forge.blueprint;

import net.fabricmc.fabric.impl.resource.loader.FabricLifecycledResourceManager;
import net.minecraft.server.packs.PackType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

// Sporran: If this is not implemented, all Fabric resource reloaders fully fail to function.
@Pseudo
@Mixin(targets = "com.teamabnormals.blueprint.common.remolder.RemoldedResourceManager")
public abstract class RemoldedResourceManagerMixin implements FabricLifecycledResourceManager {
    @Shadow @Final private PackType packType;

    @Override
    public PackType fabric_getResourceType() {
        return this.packType;
    }
}
