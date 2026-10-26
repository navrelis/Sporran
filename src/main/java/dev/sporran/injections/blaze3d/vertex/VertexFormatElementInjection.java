/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.blaze3d.vertex;

import com.mojang.blaze3d.vertex.VertexFormatElement;

public interface VertexFormatElementInjection {
    static int findNextId() {
        for (int i = 0; i < VertexFormatElement.BY_ID.length; i++) {
            if (VertexFormatElement.BY_ID[i] == null)
                return i;
        }

        throw new IllegalStateException("VertexFormatElement count limit exceeded");
    }
}
