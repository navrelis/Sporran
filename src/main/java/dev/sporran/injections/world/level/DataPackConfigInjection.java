/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.level;

import net.minecraft.world.level.DataPackConfig;
import dev.sporran.processor.FabricInjectedInterface;

import java.util.List;

@FabricInjectedInterface(DataPackConfig.class)
public interface DataPackConfigInjection {
    default void addModPacks(List<String> modPacks) {
        throw new IllegalStateException();
    }
}
