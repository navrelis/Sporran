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
