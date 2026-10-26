/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.client.gui.screens.inventory;

import net.neoforged.neoforge.client.gui.CreativeTabsScreenPage;

public interface CreativeModeInventoryScreenInjection {
    CreativeTabsScreenPage getCurrentPage();
    void setCurrentPage(CreativeTabsScreenPage currentPage);
}
