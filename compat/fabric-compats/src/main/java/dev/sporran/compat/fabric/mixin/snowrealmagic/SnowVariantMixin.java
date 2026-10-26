/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.compat.fabric.mixin.snowrealmagic;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import net.neoforged.neoforge.common.extensions.IBlockExtension;
import org.spongepowered.asm.mixin.Mixin;
import snownee.snow.block.SnowVariant;

@IfModLoaded("snowrealmagic")
@Mixin(SnowVariant.class)
public interface SnowVariantMixin extends IBlockExtension {
}
