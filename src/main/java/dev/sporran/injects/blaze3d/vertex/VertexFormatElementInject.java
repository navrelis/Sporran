// TRACKED HASH: a4d34a5fd04e3ce764b01db9dddb850217a82d89
/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.blaze3d.vertex;

import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.neoforged.fml.common.asm.enumextension.ExtensionInfo;
import net.neoforged.fml.common.asm.enumextension.IExtensibleEnum;
import net.neoforged.fml.common.asm.enumextension.NamedEnum;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.helpers.mixin.CreateStatic;
import dev.sporran.injections.blaze3d.vertex.VertexFormatElementInjection;

@Mixin(VertexFormatElement.class)
public abstract class VertexFormatElementInject implements VertexFormatElementInjection {
    @CreateStatic
    private static int findNextId() {
        return VertexFormatElementInjection.findNextId();
    }

    @NamedEnum
    @Mixin(VertexFormatElement.Usage.class)
    public abstract static class UsageInject implements IExtensibleEnum {
        @CreateStatic
        private static ExtensionInfo getExtensionInfo() {
            return ExtensionInfo.nonExtended(VertexFormatElement.Usage.class);
        }
    }
}