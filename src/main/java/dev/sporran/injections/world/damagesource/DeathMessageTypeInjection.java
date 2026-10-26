/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.damagesource;

import net.minecraft.world.damagesource.DeathMessageType;
import net.neoforged.neoforge.common.damagesource.IDeathMessageProvider;
import dev.sporran.processor.FabricInjectedInterface;
import dev.sporran.util.SporranHelper;

@FabricInjectedInterface(DeathMessageType.class)
public interface DeathMessageTypeInjection {
    default IDeathMessageProvider getMessageFunction() {
        throw SporranHelper.createMixinException(DeathMessageTypeInjection.class, "getMessageFunction");
    }
}
