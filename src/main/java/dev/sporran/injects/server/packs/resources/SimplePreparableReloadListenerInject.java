package dev.sporran.injects.server.packs.resources;

import io.github.fabricators_of_create.porting_lib.resources.conditions.ICondition;
import io.github.fabricators_of_create.porting_lib.resources.extensions.ContextAwareReloadListenerExtension;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.neoforged.neoforge.resource.ContextAwareReloadListener;
import org.spongepowered.asm.mixin.*;
import dev.sporran.helpers.mixin.Extends;
import dev.sporran.workarounds.WrappedFabricConditionContext;
import dev.sporran.workarounds.WrappedNeoConditionContext;
import dev.sporran.workarounds.ContextAwareReloadListenerWorkaround;

@Extends(ContextAwareReloadListener.class)
@Mixin(SimplePreparableReloadListener.class)
public abstract class SimplePreparableReloadListenerInject implements ContextAwareReloadListenerWorkaround, ContextAwareReloadListenerExtension {
    // Sporran: Handle Porting Lib's contexts ourselves
    public ICondition.IContext port_lib$getContext() {
        return new WrappedNeoConditionContext(this.sporran$asContextAware().getContext());
    }

    @Override
    public void injectContext(ICondition.IContext context, HolderLookup.Provider registryLookup) {
        this.sporran$asContextAware().injectContext(new WrappedFabricConditionContext(context), registryLookup);
    }
}
