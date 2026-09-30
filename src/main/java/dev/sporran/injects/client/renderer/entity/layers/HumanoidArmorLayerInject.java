package dev.sporran.injects.client.renderer.entity.layers;

import java.util.concurrent.atomic.AtomicBoolean;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dev.sporran.Sporran;
import dev.sporran.injections.client.renderer.entity.layers.HumanoidArmorLayerInjection;
import dev.sporran.util.SporranHelper;
import dev.sporran.workarounds.RenderLayerData;
import dev.sporran.workarounds.WrappedModelAsHumanoid;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;

@Implements(@Interface(iface = HumanoidArmorLayerInjection.class, prefix = "sporran$i$"))
@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerInject<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> {
    @Shadow @Final private TextureAtlas armorTrimAtlas;
    @Shadow protected abstract void renderArmorPiece(PoseStack poseStack, MultiBufferSource bufferSource, T livingEntity, EquipmentSlot slot, int packedLight, A model);

    @Unique private final RenderLayerData sporran$renderLayerData = new RenderLayerData();
    @Unique private final WrappedModelAsHumanoid sporran$wrappedHumanoidModel = new WrappedModelAsHumanoid();
    @Unique private final AtomicBoolean sporran$isRunningCompatibility = new AtomicBoolean(false);

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At("HEAD"))
    private void sporran$updateRenderLayerData(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        this.sporran$renderLayerData.update(limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
    }

    @WrapOperation(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;)V"))
    private void sporran$tryHandleRenderArmorPieceCompatibility(HumanoidArmorLayer<T, M, A> instance, PoseStack poseStack, MultiBufferSource bufferSource, T livingEntity, EquipmentSlot slot, int packedLight, A model, Operation<Void> original) {
        this.sporran$isRunningCompatibility.set(true);
        var renderData = this.sporran$renderLayerData;

        // this is probably one of the dumber things I've done but hey I mean, it works
        ((HumanoidArmorLayer<T, M, A>) (Object) this).renderArmorPiece(poseStack, bufferSource, livingEntity, slot, packedLight, model, renderData.getLimbSwing(), renderData.getLimbSwingAmount(), renderData.getPartialTicks(), renderData.getAgeInTicks(), renderData.getNetHeadYaw(), renderData.getHeadPitch());

        // Sporran: If this passes, that means it never got to run, which means we should cancel it.
        if (this.sporran$isRunningCompatibility.getAndSet(false)) {
            return;
        }

        original.call(instance, poseStack, bufferSource, livingEntity, slot, packedLight, model);
    }

    @Inject(method = "renderArmorPiece", at = @At("HEAD"))
    private void sporran$initShareData(PoseStack poseStack, MultiBufferSource bufferSource, T livingEntity, EquipmentSlot slot, int packedLight, A model, CallbackInfo ci,
                                    @Share(value = "limbSwing", namespace = Sporran.MOD_ID) LocalFloatRef limbSwing,
                                    @Share(value = "limbSwingAmount", namespace = Sporran.MOD_ID) LocalFloatRef limbSwingAmount,
                                    @Share(value = "partialTicks", namespace = Sporran.MOD_ID) LocalFloatRef partialTicks,
                                    @Share(value = "ageInTicks", namespace = Sporran.MOD_ID) LocalFloatRef ageInTicks,
                                    @Share(value = "netHeadYaw", namespace = Sporran.MOD_ID) LocalFloatRef netHeadYaw,
                                    @Share(value = "headPitch", namespace = Sporran.MOD_ID) LocalFloatRef headPitch) {
        limbSwing.set(this.sporran$renderLayerData.getLimbSwing());
        limbSwingAmount.set(this.sporran$renderLayerData.getLimbSwingAmount());
        partialTicks.set(this.sporran$renderLayerData.getPartialTicks());
        ageInTicks.set(this.sporran$renderLayerData.getAgeInTicks());
        netHeadYaw.set(this.sporran$renderLayerData.getNetHeadYaw());
        headPitch.set(this.sporran$renderLayerData.getHeadPitch());
    }

    @Inject(method = "renderArmorPiece", at= @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;setPartVisibility(Lnet/minecraft/client/model/HumanoidModel;Lnet/minecraft/world/entity/EquipmentSlot;)V", shift = At.Shift.AFTER))
    private void sporran$storeCustomArmorModel(PoseStack poseStack, MultiBufferSource bufferSource, T livingEntity, EquipmentSlot slot, int packedLight, A model, CallbackInfo ci, @Share(value = "model", namespace = Sporran.MOD_ID) LocalRef<Model> modelRef, @Local ItemStack stack) {
        modelRef.set(this.getArmorModelHook(livingEntity, stack, slot, model));
    }

    @ModifyVariable(method = "renderArmorPiece", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ArmorMaterial;layers()Ljava/util/List;"), ordinal = 1)
    private int sporran$setupCustomDefaultDyeColor(int original, PoseStack poseStack, MultiBufferSource bufferSource, T livingEntity, EquipmentSlot slot, int packedLight, A model, @Local ItemStack stack, @Share(value = "extensions", namespace = Sporran.MOD_ID) LocalRef<IClientItemExtensions> extensionsRef, @Share(value = "model", namespace = Sporran.MOD_ID) LocalRef<Model> modelRef) {
        extensionsRef.set(IClientItemExtensions.of(stack));
        if (extensionsRef.get() == IClientItemExtensions.DEFAULT)
            return original;

        extensionsRef.get().setupModelAnimations(livingEntity, stack, slot, modelRef.get(), this.sporran$renderLayerData.getLimbSwing(), this.sporran$renderLayerData.getLimbSwingAmount(), this.sporran$renderLayerData.getPartialTicks(), this.sporran$renderLayerData.getAgeInTicks(), this.sporran$renderLayerData.getNetHeadYaw(), this.sporran$renderLayerData.getHeadPitch());
        return extensionsRef.get().getDefaultDyeColor(stack);
    }

    @WrapOperation(method = "renderArmorPiece", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;renderModel(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/model/HumanoidModel;ILnet/minecraft/resources/ResourceLocation;)V"))
    private void sporran$tryRenderModel(HumanoidArmorLayer<T, M, A> instance, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, A humanoidModel, int dyeColor, ResourceLocation resourceLocation, Operation<Void> original, @Share(value = "model", namespace = Sporran.MOD_ID) LocalRef<Model> modelRef, @Share(value = "extensions", namespace = Sporran.MOD_ID) LocalRef<IClientItemExtensions> extensionsRef, @Local ItemStack stack, @Local(argsOnly = true) LivingEntity entity, @Local ArmorMaterial.Layer layer, @Local ArmorMaterial material, @Local boolean flag, @Local(argsOnly = true) EquipmentSlot slot) {
        if (extensionsRef.get() != IClientItemExtensions.DEFAULT
            || SporranHelper.INSTANCE.hasMethodOverride(this.getClass(), HumanoidArmorLayer.class, "getArmorModelHook", LivingEntity.class, ItemStack.class, EquipmentSlot.class, HumanoidModel.class)
            || SporranHelper.INSTANCE.hasMethodOverrideWithReturnType(stack.getItem().getClass(), IItemExtension.class, "getArmorTexture", ResourceLocation.class, ItemStack.class, Entity.class, EquipmentSlot.class, ArmorMaterial.Layer.class, boolean.class)
        ) {
            int color = extensionsRef.get().getArmorLayerTintColor(stack, entity, layer, material.layers().indexOf(layer), dyeColor);

            if (color != 0) {
                this.sporran$wrappedHumanoidModel.getWrapped().set(modelRef.get());
                var texture = ClientHooks.getArmorTexture(entity, stack, layer, flag, slot);
                original.call(instance, poseStack, bufferSource, packedLight, this.sporran$wrappedHumanoidModel, color, texture);
            }
        } else {
            original.call(instance, poseStack, bufferSource, packedLight, humanoidModel, dyeColor, resourceLocation);
        }
    }

    @WrapOperation(method = "renderArmorPiece", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;renderTrim(Lnet/minecraft/core/Holder;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/item/armortrim/ArmorTrim;Lnet/minecraft/client/model/HumanoidModel;Z)V"))
    private void sporran$tryUseWrapperForTrim(HumanoidArmorLayer<T, M, A> instance, Holder<ArmorMaterial> holder, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, ArmorTrim armorTrim, A humanoidModel, boolean bl, Operation<Void> original, @Share(value = "model", namespace = Sporran.MOD_ID) LocalRef<Model> modelRef, @Share(value = "extensions", namespace = Sporran.MOD_ID) LocalRef<IClientItemExtensions> extensionsRef) {
        if (extensionsRef.get() != IClientItemExtensions.DEFAULT || SporranHelper.INSTANCE.hasMethodOverride(this.getClass(), HumanoidArmorLayer.class, "getArmorModelHook", LivingEntity.class, ItemStack.class, EquipmentSlot.class, HumanoidModel.class)) {
            this.sporran$wrappedHumanoidModel.getWrapped().set(modelRef.get());
            original.call(instance, holder, poseStack, multiBufferSource, i, armorTrim, this.sporran$wrappedHumanoidModel, bl);
        } else {
            original.call(instance, holder, poseStack, multiBufferSource, i, armorTrim, humanoidModel, bl);
        }
    }

    @WrapOperation(method = "renderArmorPiece", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;renderGlint(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/model/HumanoidModel;)V"))
    private void sporran$tryUseWrapperForGlint(HumanoidArmorLayer<T, M, A> instance, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, A humanoidModel, Operation<Void> original, @Share(value = "model", namespace = Sporran.MOD_ID) LocalRef<Model> modelRef, @Share(value = "extensions", namespace = Sporran.MOD_ID) LocalRef<IClientItemExtensions> extensionsRef) {
        if (extensionsRef.get() != IClientItemExtensions.DEFAULT || SporranHelper.INSTANCE.hasMethodOverride(this.getClass(), HumanoidArmorLayer.class, "getArmorModelHook", LivingEntity.class, ItemStack.class, EquipmentSlot.class, HumanoidModel.class)) {
            this.sporran$wrappedHumanoidModel.getWrapped().set(modelRef.get());
            original.call(instance, poseStack, multiBufferSource, i, this.sporran$wrappedHumanoidModel);
        } else {
            original.call(instance, poseStack, multiBufferSource, i, humanoidModel);
        }
    }

    protected Model getArmorModelHook(T entity, ItemStack stack, EquipmentSlot slot, A model) {
        return ClientHooks.getArmorModel(entity, stack, slot, model);
    }

    public void sporran$i$renderArmorPiece(PoseStack poseStack, MultiBufferSource bufferSource, T livingEntity, EquipmentSlot slot, int packedLight, A model, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (this.sporran$isRunningCompatibility.getAndSet(false)) {
            return;
        }

        this.sporran$renderLayerData.update(limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        this.renderArmorPiece(poseStack, bufferSource, livingEntity, slot, packedLight, model);
    }

    // Sporran: let's just. copy these. Mowzie's Mobs needs these.
    protected void renderModel(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, Model model, int dyeColor, ResourceLocation textureLocation) {
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.armorCutoutNoCull(textureLocation));
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, dyeColor);
    }

    protected void renderTrim(Holder<ArmorMaterial> armorMaterial, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ArmorTrim trim, Model model, boolean innerTexture) {
        TextureAtlasSprite textureAtlasSprite = this.armorTrimAtlas.getSprite(innerTexture ? trim.innerTexture(armorMaterial) : trim.outerTexture(armorMaterial));
        VertexConsumer vertexConsumer = textureAtlasSprite.wrap(bufferSource.getBuffer(Sheets.armorTrimsSheet(trim.pattern().value().decal())));
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
    }

    protected void renderGlint(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, Model model) {
        model.renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.armorEntityGlint()), packedLight, OverlayTexture.NO_OVERLAY);
    }
}
