// TRACKED HASH: 5bd8da3c51f4d0280c72682973eb6e5b67353690
/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import dev.sporran.injections.world.inventory.SlotInjection;

@Mixin(Slot.class)
public abstract class SlotInject implements SlotInjection {
    @Shadow @Final public Container container;

    @Override
    public boolean isSameInventory(Slot other) {
        return this.container == other.container;
    }

    // Sporran: background & slot index handled by Porting Lib
}