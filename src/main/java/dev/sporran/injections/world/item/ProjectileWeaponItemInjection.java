/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import dev.sporran.util.SporranHelper;

import java.util.function.Predicate;

public interface ProjectileWeaponItemInjection {
    default Predicate<ItemStack> getSupportedHeldProjectiles(ItemStack stack) {
        throw SporranHelper.createMixinException(ProjectileWeaponItemInjection.class, "getSupportedHeldProjectiles");
    }

    default Predicate<ItemStack> getAllSupportedProjectiles(ItemStack stack) {
        throw SporranHelper.createMixinException(ProjectileWeaponItemInjection.class, "getAllSupportedProjectiles");
    }

    default AbstractArrow customArrow(AbstractArrow arrow, ItemStack projectileStack, ItemStack weaponStack) {
        throw SporranHelper.createMixinException(ProjectileWeaponItemInjection.class, "customArrow");
    }

    default ItemStack getDefaultCreativeAmmo(@Nullable Player player, ItemStack projectileWeaponItem) {
        throw SporranHelper.createMixinException(ProjectileWeaponItemInjection.class, "getDefaultCreativeAmmo");
    }
}
