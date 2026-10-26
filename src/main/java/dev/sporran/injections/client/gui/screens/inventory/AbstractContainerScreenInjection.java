/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.client.gui.screens.inventory;

import net.minecraft.world.inventory.Slot;

import java.util.concurrent.atomic.AtomicInteger;

public interface AbstractContainerScreenInjection {
    AtomicInteger sporran$slotColor = new AtomicInteger(-2130706433);
    int defaultSlotColor = -2130706433;

    Slot getSlotUnderMouse();
    int getGuiLeft();
    int getGuiTop();
    int getXSize();
    int getYSize();
    int getSlotColor(int index);
    void sporran$setSlotColor(int slotColor);
}
