/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.client.gui.screens.worldselection;

import net.minecraft.client.gui.screens.worldselection.PresetEditor;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PresetEditor.class)
public interface PresetEditorInject {
    // Sporran: there's only a Deprecated tag in here.
}
