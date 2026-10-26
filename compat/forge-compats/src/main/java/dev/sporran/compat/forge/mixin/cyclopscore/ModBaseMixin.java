/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.compat.forge.mixin.cyclopscore;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

@IfModLoaded("cyclopscore")
@Pseudo
@Mixin(targets = "org.cyclops.cyclopscore.init.ModBase")
public class ModBaseMixin {
//    @Dynamic
//    @ModifyExpressionValue(method = "beforeRegistriedFilled", at = @At(value = "INVOKE", target = "Ljava/lang/Object;equals(Ljava/lang/Object;)Z"))
//    private boolean sporran$fixDifferentRegistryOrder(boolean original, RegisterEvent event) {
//        return event.getRegistryKey().equals(Registries.PARTICLE_TYPE);
//    }
}
