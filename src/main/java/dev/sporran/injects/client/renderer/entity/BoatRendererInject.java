/*
 * Changed by the Sporran project between 2026-09-29 and 2026-10-04; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.client.renderer.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import dev.sporran.injections.client.renderer.entity.BoatRendererInjection;
import dev.sporran.util.SporranHelper;

import java.util.Map;

@Mixin(BoatRenderer.class)
public abstract class BoatRendererInject implements BoatRendererInjection {
    @Shadow @Final private Map<Boat.Type, Pair<ResourceLocation, ListModel<Boat>>> boatResources;

    // Sporran: runs for every boat in every frame, resolve the override check only once.
    @Unique private static final SporranHelper.MethodOverrideCheck sporran$GET_MODEL_WITH_LOCATION_OVERRIDE = SporranHelper.MethodOverrideCheck.of(BoatRenderer.class, "getModelWithLocation", Boat.class);

    @WrapOperation(
            method = "getTextureLocation(Lnet/minecraft/world/entity/vehicle/Boat$Type;Z)Lnet/minecraft/resources/ResourceLocation;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/resources/ResourceLocation;withDefaultNamespace(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;"
            )
    )
    private static ResourceLocation sporran$getNamespacedTextureLocation(String texture, Operation<ResourceLocation> original) {
        if (texture.contains(":")) {
            String brokenPrefix = StringUtils.substringBefore(texture, ":");
            String namespace = StringUtils.substringAfterLast(brokenPrefix, "/");
            String prefix = StringUtils.substringBeforeLast(brokenPrefix, "/") + "/";
            String suffix = StringUtils.substringAfter(texture, ":");
            ResourceLocation id = ResourceLocation.tryParse(namespace + ":" + prefix + suffix);
            if (id != null) return id;
        }
        return original.call(texture);
    }

    @WrapOperation(method = {"getTextureLocation(Lnet/minecraft/world/entity/vehicle/Boat;)Lnet/minecraft/resources/ResourceLocation;", "render(Lnet/minecraft/world/entity/vehicle/Boat;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"}, at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 0))
    private <K, V> V sporran$tryUseForgeGetModel(Map<K, V> instance, Object o, Operation<V> original, @Local(argsOnly = true) Boat boat) {
        if (sporran$GET_MODEL_WITH_LOCATION_OVERRIDE.test(this.getClass())) {
            return (V) this.getModelWithLocation(boat);
        }

        return original.call(instance, o);
    }

    @Override
    public Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(Boat boat) {
        return this.boatResources.get(boat.getVariant());
    }
}
