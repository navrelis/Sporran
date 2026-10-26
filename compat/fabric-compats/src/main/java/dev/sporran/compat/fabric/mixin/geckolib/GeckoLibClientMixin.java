/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.compat.fabric.mixin.geckolib;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import org.spongepowered.asm.mixin.Mixin;
import software.bernie.geckolib.GeckoLibClient;
import dev.sporran.helpers.mixin.CreateStatic;

@IfModLoaded("geckolib")
@Mixin(GeckoLibClient.class)
public abstract class GeckoLibClientMixin {
    @CreateStatic
    private static void init() {
        // This isn't API, why do people do this?
    }
}
