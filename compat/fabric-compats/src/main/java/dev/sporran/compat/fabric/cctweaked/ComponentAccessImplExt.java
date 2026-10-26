/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.compat.fabric.cctweaked;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapability;

import java.util.function.Consumer;

public interface ComponentAccessImplExt {
    void sporran$initializeCapabilityLookups(BlockCapability<?, Direction> capability, Consumer<Direction> invalidate);
}
