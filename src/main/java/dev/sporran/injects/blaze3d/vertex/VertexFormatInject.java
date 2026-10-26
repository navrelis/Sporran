// TRACKED HASH: a603a767feff06f5cab970bb1912dd85f074516c
/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.blaze3d.vertex;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import dev.sporran.injections.blaze3d.vertex.VertexFormatInjection;

import java.util.List;

@Mixin(VertexFormat.class)
public abstract class VertexFormatInject implements VertexFormatInjection {
    @Shadow @Final private List<VertexFormatElement> elements;
    @Shadow @Final private List<String> names;

    @Override
    public ImmutableMap<String, VertexFormatElement> getElementMapping() {
        ImmutableMap.Builder<String, VertexFormatElement> builder = ImmutableMap.builder();
        for (int i = 0; i < elements.size(); i++) {
            builder.put(names.get(i), elements.get(i));
        }

        return builder.build();
    }

    @Override
    public boolean hasPosition() {
        return this.elements.stream().anyMatch(e -> e.usage() == VertexFormatElement.Usage.POSITION);
    }

    @Override
    public boolean hasNormal() {
        return this.elements.stream().anyMatch(e -> e.usage() == VertexFormatElement.Usage.NORMAL);
    }

    @Override
    public boolean hasColor() {
        return this.elements.stream().anyMatch(e -> e.usage() == VertexFormatElement.Usage.COLOR);
    }

    @Override
    public boolean hasUV(int which) {
        return this.elements.stream().anyMatch(e -> e.usage() == VertexFormatElement.Usage.UV && e.index() == which);
    }
}