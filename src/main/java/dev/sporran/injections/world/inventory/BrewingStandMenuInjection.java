package dev.sporran.injections.world.inventory;

import net.minecraft.world.item.alchemy.PotionBrewing;

public interface BrewingStandMenuInjection {
    interface PotionSlotInjection {
        void sporran$setPotionBrewing(PotionBrewing brewing);
    }
}
