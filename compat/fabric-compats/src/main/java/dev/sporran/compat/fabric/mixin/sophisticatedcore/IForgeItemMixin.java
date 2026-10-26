/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.compat.fabric.mixin.sophisticatedcore;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import net.p3pp3rf1y.sophisticatedcore.extensions.item.SophisticatedItem;
import org.spongepowered.asm.mixin.Mixin;

@IfModLoaded("sophisticatedcore")
@Mixin(IItemExtension.class)
public interface IForgeItemMixin extends SophisticatedItem {
}
