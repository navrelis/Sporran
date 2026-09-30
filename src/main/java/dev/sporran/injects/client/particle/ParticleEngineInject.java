package dev.sporran.injects.client.particle;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.google.common.collect.Maps;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL32C;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dev.sporran.Sporran;
import dev.sporran.injections.client.particle.ParticleEngineInjection;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

@Mixin(ParticleEngine.class)
public abstract class ParticleEngineInject implements ParticleEngineInjection {
    @Shadow @Final @Mutable private Map<ParticleRenderType, Queue<Particle>> particles;
    @Shadow @Final private static List<ParticleRenderType> RENDER_ORDER;
    @Shadow protected ClientLevel level;
    @Shadow public abstract void crack(BlockPos pos, Direction side);

    @Shadow public abstract void render(LightTexture lightTexture, Camera camera, float f);

    @Unique private @Nullable Frustum sporran$clippingHelper;
    @Unique private Predicate<ParticleRenderType> sporran$renderTypePredicate;

    // Used by some Forge mods, so we need to patch it, but unfortunately also means we're storing this data twice.
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @Unique private final Map<ResourceLocation, ParticleProvider<?>> sporran$providers = new HashMap<>();

    @WrapOperation(method = {"register(Lnet/minecraft/core/particles/ParticleType;Lnet/minecraft/client/particle/ParticleProvider;)V", "register(Lnet/minecraft/core/particles/ParticleType;Lnet/minecraft/client/particle/ParticleEngine$SpriteParticleRegistration;)V"}, at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;put(ILjava/lang/Object;)Ljava/lang/Object;", remap = false))
    private <T extends ParticleOptions> Object sporran$registerToForgeProviders(Int2ObjectMap<?> instance, int i, Object o, Operation<Object> original, @Local(argsOnly = true) ParticleType<T> particleType) {
        this.sporran$providers.put(BuiltInRegistries.PARTICLE_TYPE.getKey(particleType), (ParticleProvider<?>) o);
        return original.call(instance, i, o);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void sporran$handleParticleTypeComparator(ClientLevel level, TextureManager textureManager, CallbackInfo ci) {
        var oldMap = this.particles;
        this.particles = Maps.newTreeMap(ClientHooks.makeParticleRenderTypeComparator(RENDER_ORDER));

        this.particles.putAll(oldMap);
    }

    @Override
    public void sporran$setClippingHelper(Frustum frustum) {
        this.sporran$clippingHelper = frustum;
    }

    @Override
    public void render(LightTexture lightTexture, Camera camera, float tickDelta, @Nullable Frustum clippingHelper, Predicate<ParticleRenderType> renderTypePredicate) {
        this.sporran$clippingHelper = clippingHelper;
        this.sporran$renderTypePredicate = renderTypePredicate;
        this.render(lightTexture, camera, tickDelta);
        this.sporran$clippingHelper = null;
        this.sporran$renderTypePredicate = null;
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void sporran$initShareData(LightTexture lightTexture, Camera camera, float partialTick, CallbackInfo ci,
                                    @Share(value = "frustum", namespace = Sporran.MOD_ID) LocalRef<Frustum> frustum,
                                    @Share(value = "renderTypePredicate", namespace = Sporran.MOD_ID) LocalRef<Predicate<ParticleRenderType>> renderTypePredicate) {
        frustum.set(this.sporran$clippingHelper);
        renderTypePredicate.set(Objects.requireNonNullElse(this.sporran$renderTypePredicate, $ -> true));
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;enableDepthTest()V", shift = At.Shift.AFTER, ordinal = 0, remap = false))
    private void sporran$initActiveTexture(LightTexture lightTexture, Camera camera, float partialTick, CallbackInfo ci) {
        RenderSystem.activeTexture(GL32C.GL_TEXTURE2);
        RenderSystem.activeTexture(GL32C.GL_TEXTURE0);
    }

    @ModifyExpressionValue(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/particle/ParticleEngine;RENDER_ORDER:Ljava/util/List;"))
    private List<ParticleRenderType> sporran$mergeCustomParticles(List<ParticleRenderType> original, @Share(value = "renderTypePredicate", namespace = Sporran.MOD_ID) LocalRef<Predicate<ParticleRenderType>> renderTypePredicate) {
        var set = new LinkedHashSet<ParticleRenderType>();
        set.addAll(RENDER_ORDER);
        set.addAll(this.particles.keySet());

        var predicate = renderTypePredicate.get();
        if (predicate == null)
            predicate = $ -> true;

        set.removeIf(type -> type == ParticleRenderType.NO_RENDER);

        Predicate<ParticleRenderType> finalPredicate = predicate;
        set.removeIf(t -> !finalPredicate.test(t));

        return set.stream().toList();
    }

//    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"))
//    private <V> V sporran$removeIfClippingHelper(V original) {
//        if (sporran$clippingHelper != null) {
//            var list = (Iterable<Particle>) original;
//            var clippingHelper = sporran$clippingHelper;
//            return (V) Streams.stream(list).filter(p -> !(p.shouldCull() && !clippingHelper.isVisible(p.getBoundingBox()))).toList();
//        }
//
//        return original;
//    }

    @ModifyExpressionValue(method = "destroy", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;shouldSpawnTerrainParticles()Z"))
    private boolean sporran$callDestroyEffects(boolean original, @Local(argsOnly = true) BlockPos pos, @Local(argsOnly = true) BlockState state) {
        return original || !IClientBlockExtensions.of(state).addDestroyEffects(state, this.level, pos, (ParticleEngine) (Object) this);
    }

    @ModifyExpressionValue(method = {"method_34020", "crack"}, at = @At(value = "NEW", target = "(Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/client/particle/TerrainParticle;"))
    private TerrainParticle sporran$handleUpdateSprite(TerrainParticle original, @Local BlockState state, @Local(argsOnly = true) BlockPos pos) {
        return original.updateSprite(state, pos);
    }

    @Override
    public void iterateParticles(Consumer<Particle> consumer) {
        for (ParticleRenderType particleRenderType : this.particles.keySet()) {
            if (particleRenderType == ParticleRenderType.NO_RENDER)
                continue;

            Iterable<Particle> particles = this.particles.get(particleRenderType);
            if (particles != null) {
                particles.forEach(consumer);
            }
        }
    }

    @Override
    public void addBlockHitEffects(BlockPos pos, BlockHitResult target) {
        sporran$addBlockHitEffects(pos, target, target.getDirection(), args -> {
            ((ParticleEngine) args[0]).crack((BlockPos) args[1], (Direction) args[2]);
            return null;
        });
    }

    @Override
    public void sporran$addBlockHitEffects(BlockPos pos, BlockHitResult target, Direction direction, Operation<Void> original) {
        var state = this.level.getBlockState(pos);

        if (!IClientBlockExtensions.of(state).addHitEffects(state, this.level, target, (ParticleEngine) (Object) this))
            original.call(this, pos, direction);
    }

    @Override
    public Map<ResourceLocation, ParticleProvider<?>> sporran$getProviders() {
        return this.sporran$providers;
    }
}
