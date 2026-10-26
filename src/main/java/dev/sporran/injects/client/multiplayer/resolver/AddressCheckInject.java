/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.client.multiplayer.resolver;

import net.minecraft.client.multiplayer.resolver.AddressCheck;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AddressCheck.class)
public interface AddressCheckInject {
    // Sporran: this patch only actually makes it load under the module class loader, which Sporran intentionally avoids using entirely.
}
