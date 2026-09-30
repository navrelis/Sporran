// TRACKED HASH: 5520e72d813534639a4a101cffa56e6e9fe409b0
package dev.sporran.injects.world.level.levelgen.structure.templatesystem;

import com.google.common.collect.Maps;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dev.sporran.helpers.mixin.CreateStatic;
import dev.sporran.injections.world.level.levelgen.structure.templatesystem.StructureProcessorInjection;
import dev.sporran.injections.world.level.levelgen.structure.templatesystem.StructureTemplateInjection;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

@Mixin(StructureTemplate.class)
public abstract class StructureTemplateInject implements StructureTemplateInjection {
    @CreateStatic
    private static Vec3 forge$transformedVec3d(StructurePlaceSettings placementIn, Vec3 pos) {
        return StructureTemplateInjection.transformedVec3d(placementIn, pos);
    }

    @CreateStatic
    private static List<StructureTemplate.StructureEntityInfo> forge$processEntityInfos(@Nullable StructureTemplate template, LevelAccessor level, BlockPos blockPos, StructurePlaceSettings structurePlaceSettings, List<StructureTemplate.StructureEntityInfo> structureEntityInfoList) {
        return StructureTemplateInjection.processEntityInfos(template, level, blockPos, structurePlaceSettings, structureEntityInfoList);
    }

    // Sporran: worldgen worker threads and the server thread place structures at the same time, so the template
    // handed over by NeoForge's 6-arg processBlockInfos must be per-thread, not shared.
    private static final ThreadLocal<StructureTemplate> sporran$template = new ThreadLocal<>();

    @Shadow
    public static Vec3 transform(Vec3 target, Mirror mirror, Rotation rotation, BlockPos centerOffset) {
        return null;
    }

    @CreateStatic
    private static List<StructureTemplate.StructureBlockInfo> processBlockInfos(ServerLevelAccessor level, BlockPos pos, BlockPos pos2, StructurePlaceSettings structurePlaceSettings, List<StructureTemplate.StructureBlockInfo> list, @Nullable StructureTemplate template) {
        // Sporran: restore the previous value afterwards so nested placements on the same thread keep working.
        var previous = sporran$template.get();
        sporran$template.set(template);
        try {
            return StructureTemplate.processBlockInfos(level, pos, pos2, structurePlaceSettings, list);
        } finally {
            if (previous == null)
                sporran$template.remove();
            else
                sporran$template.set(previous);
        }
    }

    @WrapOperation(method = "processBlockInfos", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessor;processBlock(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$StructureBlockInfo;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$StructureBlockInfo;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;)Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$StructureBlockInfo;"))
    private static StructureTemplate.StructureBlockInfo sporran$useForgeProcess(StructureProcessor instance, LevelReader level, BlockPos offset, BlockPos pos, StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo, StructurePlaceSettings settings, Operation<StructureTemplate.StructureBlockInfo> original) {
        // Sporran: like NeoForge, every processor of every block goes through process(...); the template is null
        // when placing through vanilla's 5-arg processBlockInfos. The default process() delegates to processBlock.
        return ((StructureProcessorInjection) instance).process(level, offset, pos, blockInfo, relativeBlockInfo, settings, sporran$template.get());
    }

    @Mixin(StructureTemplate.Palette.class)
    public abstract static class PaletteInject {
        @Shadow @Final @Mutable private Map<Block, List<StructureTemplate.StructureBlockInfo>> cache;

        // Neo: Fixes MC-271899 - templates are shared by all worldgen threads and the server thread, and the vanilla
        // HashMap cache throws ConcurrentModificationException when two threads fill it at the same time.
        @Inject(method = "<init>", at = @At("RETURN"))
        private void sporran$useConcurrentCache(List<StructureTemplate.StructureBlockInfo> blocks, CallbackInfo ci) {
            this.cache = Maps.newConcurrentMap();
        }
    }
}