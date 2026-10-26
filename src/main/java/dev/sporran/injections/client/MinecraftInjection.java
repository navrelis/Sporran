/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import dev.sporran.processor.FabricInjectedInterface;

@FabricInjectedInterface(Minecraft.class)
public interface MinecraftInjection {
    default ItemColors getItemColors() {
        throw new IllegalStateException();
    }
}
