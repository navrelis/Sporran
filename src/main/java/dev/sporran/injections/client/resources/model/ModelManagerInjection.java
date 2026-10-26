/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.client.resources.model;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelManager;
import dev.sporran.processor.FabricInjectedInterface;

@FabricInjectedInterface(ModelManager.class)
public interface ModelManagerInjection {
    ModelBakery getModelBakery();
}
