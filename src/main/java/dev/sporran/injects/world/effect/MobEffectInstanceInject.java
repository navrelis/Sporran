// TRACKED HASH: c90ad9c5c8bd04fe0240bdfe4249e3f318e2cd46
package dev.sporran.injects.world.effect;

import com.google.common.collect.Sets;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.common.EffectCure;
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dev.sporran.helpers.mixin.CreateInitializer;
import dev.sporran.helpers.mixin.CreateStatic;
import dev.sporran.injections.world.effect.MobEffectInstanceInjection;
import dev.sporran.util.SporranHelper;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@Implements(@Interface(iface = MobEffectInstanceInjection.class, prefix = "sporran$i$"))
@Mixin(value = MobEffectInstance.class, priority = 1010)
public abstract class MobEffectInstanceInject implements MobEffectInstanceInjection {
    @Shadow @Final private Holder<MobEffect> effect;

    @Inject(method = "<init>(Lnet/minecraft/core/Holder;IIZZZLnet/minecraft/world/effect/MobEffectInstance;)V", at = @At("TAIL"))
    private void sporran$storeEffectCures(Holder effect, int duration, int amplifier, boolean ambient, boolean visible, boolean showIcon, MobEffectInstance hiddenEffect, CallbackInfo ci) {
        this.effect.value().neo$fillEffectCures(this.cures, (MobEffectInstance) (Object) this);
    }

    @Inject(method = "<init>(Lnet/minecraft/core/Holder;Lnet/minecraft/world/effect/MobEffectInstance$Details;)V", at = @At("TAIL"))
    private void sporran$copyAllCuresFromDetails(Holder<MobEffect> effect, MobEffectInstance.Details details, CallbackInfo ci) {
        this.cures.clear();
        ((MobEffectInstanceInjection.DetailsInjection) (Object) details).cures().ifPresent(this.cures::addAll);
    }

    @ModifyReturnValue(method = "asDetails", at = @At("RETURN"))
    private MobEffectInstance.Details sporran$attachCuresToDetails(MobEffectInstance.Details original) {
        ((MobEffectInstanceInjection.DetailsInjection) (Object) original).sporran$setCures(Optional.of(this.neoforge$getCures()).filter(cures -> !cures.isEmpty()));
        return original;
    }

    @Inject(method = "setDetailsFrom", at = @At("TAIL"))
    private void sporran$copyCuresFromOther(MobEffectInstance effectInstance, CallbackInfo ci) {
        this.cures.clear();
        this.cures.addAll(effectInstance.neoforge$getCures());
    }

    // TODO: impl sort order

    @Unique private final Set<EffectCure> cures = Sets.newIdentityHashSet();

    @Override
    public Set<EffectCure> neoforge$getCures() {
        return cures;
    }

    @Mixin(MobEffectInstance.Details.class)
    public abstract static class DetailsInject implements MobEffectInstanceInjection.DetailsInjection {
        @Unique private Optional<Set<EffectCure>> cures = Optional.empty();

        @Override
        public Optional<Set<EffectCure>> cures() {
            return this.cures;
        }

        @Override
        public void sporran$setCures(Optional<Set<EffectCure>> cures) {
            this.cures = cures;
        }

        public DetailsInject(int amplifier, int duration, boolean ambient, boolean showParticles, boolean showIcon, Optional<MobEffectInstance.Details> hiddenEffect) {}

        @CreateInitializer
        public DetailsInject(int amplifier, int duration, boolean ambient, boolean showParticles, boolean showIcon, Optional<MobEffectInstance.Details> hiddenEffect, Optional<Set<EffectCure>> cures) {
            this(amplifier, duration, ambient, showParticles, showIcon, hiddenEffect);
            this.cures = cures;
        }

        @ModifyReturnValue(method = "method_56672", at = @At("RETURN"))
        private static App<RecordCodecBuilder.Mu<MobEffectInstance.Details>, MobEffectInstance.Details> sporran$appendNeoCuresCodec(App<RecordCodecBuilder.Mu<MobEffectInstance.Details>, MobEffectInstance.Details> original, @Local(argsOnly = true) RecordCodecBuilder.Instance<MobEffectInstance.Details> instance) {
            return instance.group(
                original,
                NeoForgeExtraCodecs.setOf(EffectCure.CODEC)
                    .optionalFieldOf("neoforge:cures")
                    .forGetter(details -> ((MobEffectInstanceInjection.DetailsInjection) (Object) details).cures())
            )
                .apply(instance, (details, cures) -> {
                    ((MobEffectInstanceInjection.DetailsInjection) (Object) details).sporran$setCures(cures);
                    return details;
                });
        }

        @ModifyReturnValue(method = "method_57279", at = @At("RETURN"))
        private static StreamCodec<RegistryFriendlyByteBuf, MobEffectInstance.Details> sporran$appendNeoCuresStreamCodec(StreamCodec<ByteBuf, MobEffectInstance.Details> original) {
            return StreamCodec.composite(
                // I feel like i should be concerned that this just works.
                original, Function.identity(),

                NeoForgeStreamCodecs.connectionAware(
                    ByteBufCodecs.optional(EffectCure.STREAM_CODEC.apply(ByteBufCodecs.collection(HashSet::new))),
                    NeoForgeStreamCodecs.uncheckedUnit(Optional.empty())
                ), details -> ((MobEffectInstanceInjection.DetailsInjection) (Object) details).cures(),

                (details, cures) -> {
                    ((MobEffectInstanceInjection.DetailsInjection) (Object) details).sporran$setCures(cures);
                    return details;
                }
            );
        }

        // but why though?
        @CreateStatic
        private static MobEffectInstance.Details create(int amplifier, int duration, boolean ambient, boolean showParticles, Optional<Boolean> showIcon, Optional<MobEffectInstance.Details> hiddenEffect, Optional<Set<EffectCure>> cures) {
            var value = new MobEffectInstance.Details(amplifier, duration, ambient, showParticles, showIcon.orElse(showParticles), hiddenEffect);
            ((MobEffectInstanceInjection.DetailsInjection) (Object) value).sporran$setCures(cures);
            return value;
        }
    }
}