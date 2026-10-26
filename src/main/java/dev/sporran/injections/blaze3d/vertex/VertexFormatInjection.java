/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.blaze3d.vertex;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.VertexFormatElement;

public interface VertexFormatInjection {
    ImmutableMap<String, VertexFormatElement> getElementMapping();
    boolean hasPosition();
    boolean hasNormal();
    boolean hasColor();
    boolean hasUV(int which);
}
