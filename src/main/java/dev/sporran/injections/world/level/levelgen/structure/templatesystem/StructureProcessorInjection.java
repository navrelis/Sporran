package dev.sporran.injections.world.level.levelgen.structure.templatesystem;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import dev.sporran.util.SporranHelper;

import javax.annotation.Nullable;

public interface StructureProcessorInjection {
    @Nullable
    default StructureTemplate.StructureBlockInfo process(LevelReader level, BlockPos blockPos, BlockPos pos, StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo, StructurePlaceSettings settings, @org.jetbrains.annotations.Nullable StructureTemplate template) {
        throw SporranHelper.createMixinException(StructureProcessorInjection.class, "process");
    }

    default StructureTemplate.StructureEntityInfo processEntity(LevelReader world, BlockPos seedPos, StructureTemplate.StructureEntityInfo rawEntityInfo, StructureTemplate.StructureEntityInfo entityInfo, StructurePlaceSettings placementSettings, StructureTemplate template) {
        throw SporranHelper.createMixinException(StructureProcessorInjection.class, "processEntity");
    }
}
