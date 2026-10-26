/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.core.dispenser;

import net.minecraft.core.dispenser.BoatDispenseItemBehavior;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BoatDispenseItemBehavior.class)
public abstract class BoatDispenseItemBehaviorInject extends DefaultDispenseItemBehavior {
    // TODO: how on earth do we accomplish this one
}
