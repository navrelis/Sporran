/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.server;

import net.minecraft.core.HolderLookup;
import net.minecraft.server.ReloadableServerResources;
import net.neoforged.neoforge.common.conditions.ICondition;
import dev.sporran.processor.FabricInjectedInterface;

@FabricInjectedInterface(ReloadableServerResources.class)
public interface ReloadableServerResourcesInjection {
    default ICondition.IContext getConditionContext() {
        throw new RuntimeException("mixin, why didn't you add this");
    }

    default HolderLookup.Provider getRegistryLookup() {
        throw new RuntimeException("mixin, why didn't you add this");
    }
}