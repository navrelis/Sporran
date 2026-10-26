/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.workarounds;

import net.minecraft.resources.RegistryOps;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.resource.ContextAwareReloadListener;

public interface ContextAwareReloadListenerWorkaround {
    default ContextAwareReloadListener sporran$asContextAware() {
        return (ContextAwareReloadListener) this;
    }

    default <T> ConditionalOps<T> sporran$makeConditionalOps(RegistryOps<T> original) {
        return new ConditionalOps<>(original, this.sporran$asContextAware().getContext());
    }
}
