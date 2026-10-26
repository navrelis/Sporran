/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.entity.animal;

import net.minecraft.world.entity.animal.Sheep;
import net.neoforged.neoforge.common.IShearable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Sheep.class)
public abstract class SheepInject implements IShearable {
    // Sporran: Handled by Porting Lib
}
