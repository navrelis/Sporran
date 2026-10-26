/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.inventory;

import net.minecraft.world.item.alchemy.PotionBrewing;

public interface BrewingStandMenuInjection {
    interface PotionSlotInjection {
        void sporran$setPotionBrewing(PotionBrewing brewing);
    }
}
