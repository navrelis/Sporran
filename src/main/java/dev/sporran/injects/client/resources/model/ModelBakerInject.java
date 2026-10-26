// TRACKED HASH: e63986ae2aac1456fe256ecfa6d01712dbfbbc8f
/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.client.resources.model;

import net.minecraft.client.resources.model.ModelBaker;
import net.neoforged.neoforge.client.extensions.IModelBakerExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ModelBaker.class)
public interface ModelBakerInject extends IModelBakerExtension {
}