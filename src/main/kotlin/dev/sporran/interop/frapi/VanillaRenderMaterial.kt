/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.interop.frapi

import com.mojang.blaze3d.vertex.VertexFormatElement
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial
import net.fabricmc.fabric.api.util.TriState
import net.minecraft.client.renderer.RenderType

class VanillaRenderMaterial(val renderType: RenderType) : RenderMaterial {
    override fun blendMode(): BlendMode {
        return BlendMode.fromRenderLayer(renderType)
    }

    override fun disableColorIndex(): Boolean {
        return renderType.format().elements.none { it.usage == VertexFormatElement.Usage.COLOR }
    }

    override fun emissive(): Boolean {
        // TODO: figure out how to detect this
        return false
    }

    override fun disableDiffuse(): Boolean {
        return renderType.format().elements.none { it.usage == VertexFormatElement.Usage.COLOR || it.usage == VertexFormatElement.Usage.UV }
    }

    override fun ambientOcclusion(): TriState {
        return TriState.DEFAULT
    }

    override fun glint(): TriState {
        return TriState.DEFAULT
    }
}