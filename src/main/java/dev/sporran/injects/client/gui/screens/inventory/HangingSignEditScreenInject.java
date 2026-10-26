/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.client.gui.screens.inventory;

import net.minecraft.client.gui.screens.inventory.HangingSignEditScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HangingSignEditScreen.class)
public abstract class HangingSignEditScreenInject {
    // Sporran: handled by Fabric API
}
