package dev.sporran.injections.client.renderer.chunk;

import java.util.List;

import com.mojang.blaze3d.vertex.VertexSorting;
import net.neoforged.neoforge.client.event.AddSectionGeometryEvent;
import dev.sporran.util.SporranHelper;

import net.minecraft.client.renderer.SectionBufferBuilderPack;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.client.renderer.chunk.SectionCompiler;
import net.minecraft.core.SectionPos;

public interface SectionCompilerInjection {
    default void sporran$setAdditionalRenderers(List<AddSectionGeometryEvent.AdditionalSectionRenderer> renderers) {
        throw SporranHelper.createMixinException(SectionCompilerInjection.class, "sporran$setAdditionalRenderers");
    }

    default SectionCompiler.Results compile(SectionPos pos, RenderChunkRegion region, VertexSorting sorting, SectionBufferBuilderPack bufferBuilderPack, List<AddSectionGeometryEvent.AdditionalSectionRenderer> additionalRenderers) {
        throw SporranHelper.createMixinException(SectionCompilerInjection.class, "compile");
    }
}
