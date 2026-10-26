/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.client;

import net.minecraft.client.User;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(User.class)
public abstract class UserInject {
    // Sporran: no
}
