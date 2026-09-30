package dev.sporran.compat.create.mixin.flywheel;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;

@IfModLoaded("flywheel")
@Mixin(targets = "dev.engine_room.flywheel.lib.model.baked.FabricMeshEmitterManager")
public interface FabricMeshEmitterManagerAccessor {
    @Invoker("prepareForModel")
    BakedModel sporran$callPrepareForModel(BakedModel model, RenderType defaultLayer, boolean useAo, boolean defaultAo);
}
