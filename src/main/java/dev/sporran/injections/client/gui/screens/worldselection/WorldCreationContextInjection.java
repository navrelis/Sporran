/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.client.gui.screens.worldselection;

import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.world.level.WorldDataConfiguration;

public interface WorldCreationContextInjection {
    WorldCreationContext withDataConfiguration(WorldDataConfiguration dataConfiguration);
}
