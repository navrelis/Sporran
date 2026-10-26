/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.level.levelgen.structure;

import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.neoforge.common.world.ModifiableStructureInfo;

public interface StructureInjection {
    default ModifiableStructureInfo modifiableStructureInfo() {
        throw new IllegalStateException();
    }

    default Structure.StructureSettings getModifiedStructureSettings() {
        throw new IllegalStateException();
    }
}
