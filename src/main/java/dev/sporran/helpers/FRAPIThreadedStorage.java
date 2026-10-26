/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;

public class FRAPIThreadedStorage {
    public static final ThreadLocal<Level> LEVEL = ThreadLocal.withInitial(() -> null);
    public static final ThreadLocal<BlockPos> POS = ThreadLocal.withInitial(() -> null);
}
