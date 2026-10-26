/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.client.gui.font.providers;

import net.neoforged.fml.common.asm.enumextension.ExtensionInfo;
import net.neoforged.fml.common.asm.enumextension.IExtensibleEnum;
import net.neoforged.fml.common.asm.enumextension.NamedEnum;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.helpers.mixin.CreateStatic;

import net.minecraft.client.gui.font.providers.GlyphProviderType;

@NamedEnum
@Mixin(GlyphProviderType.class)
public abstract class GlyphProviderTypeInject implements IExtensibleEnum {
    @CreateStatic
    private static ExtensionInfo getExtensionInfo() {
        return ExtensionInfo.nonExtended(GlyphProviderType.class);
    }
}
