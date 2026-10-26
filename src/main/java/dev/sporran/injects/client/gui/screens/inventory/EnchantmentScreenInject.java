/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.client.gui.screens.inventory;

import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EnchantmentScreen.class)
public abstract class EnchantmentScreenInject {
    // TODO: this one's incredibly difficult... we'll avoid it for now unless something highly relies on this behaviour
}
