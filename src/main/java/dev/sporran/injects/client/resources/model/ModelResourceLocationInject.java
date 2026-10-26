/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.client.resources.model;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.helpers.mixin.CreateStatic;
import dev.sporran.injections.client.resources.model.ModelResourceLocationInjection;

@Mixin(ModelResourceLocation.class)
public abstract class ModelResourceLocationInject {
    @CreateStatic
    private static final String STANDALONE_VARIANT = ModelResourceLocationInjection.STANDALONE_VARIANT;

    @CreateStatic
    private static ModelResourceLocation standalone(ResourceLocation id) {
        return ModelResourceLocationInjection.standalone(id);
    }
}
