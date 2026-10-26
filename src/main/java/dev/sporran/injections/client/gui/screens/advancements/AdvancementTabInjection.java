/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.client.gui.screens.advancements;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementNode;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.advancements.AdvancementTab;
import net.minecraft.client.gui.screens.advancements.AdvancementTabType;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;

public interface AdvancementTabInjection {
    int getPage();
    void sporran$setPage(int page);
    void sporran$setIndex(int index);

    static AdvancementTab create(Minecraft mc, AdvancementsScreen screen, AdvancementTabType type, int index, int page, AdvancementNode adv, DisplayInfo info) {
        var tab = new AdvancementTab(mc, screen, type, index, adv, info);
        ((AdvancementTabInjection) tab).sporran$setPage(page);

        return tab;
    }
}
