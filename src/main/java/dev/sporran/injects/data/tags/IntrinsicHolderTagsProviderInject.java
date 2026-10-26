/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.data.tags;

import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.extensions.IIntrinsicHolderTagAppenderExtension;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Function;

@Mixin(IntrinsicHolderTagsProvider.class)
public abstract class IntrinsicHolderTagsProviderInject {
    // Sporran: we have no reason to implement this

    @Mixin(IntrinsicHolderTagsProvider.IntrinsicTagAppender.class)
    public abstract static class IntrinsicTagAppenderInject<T> implements IIntrinsicHolderTagAppenderExtension<T> {
        @Shadow @Final private Function<T, ResourceKey<T>> keyExtractor;

        @Override
        public ResourceKey<T> getKey(T value) {
            return this.keyExtractor.apply(value);
        }
    }
}
