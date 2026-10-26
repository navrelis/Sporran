/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.level.block.state.properties;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import dev.sporran.mixin.WoodTypeAccessor;

public interface WoodTypeInjection {
    static WoodType create(String name, BlockSetType setType) {
        return WoodTypeAccessor.createWoodType(name, setType);
    }
}
