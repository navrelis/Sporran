package dev.sporran.injects.client.renderer.blockentity;

import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.injections.world.phys.AABBInjection;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.StructureBlockRenderer;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.phys.AABB;

@Mixin(StructureBlockRenderer.class)
public abstract class StructureBlockRendererInject implements BlockEntityRenderer<StructureBlockEntity> {
    @Override
    public AABB getRenderBoundingBox(StructureBlockEntity blockEntity) {
        return AABBInjection.INFINITE;
    }
}
