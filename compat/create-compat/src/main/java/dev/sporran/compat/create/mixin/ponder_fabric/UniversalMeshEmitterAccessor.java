/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.compat.create.mixin.ponder_fabric;

import net.createmod.catnip.client.render.model.ShadeSeparatedBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.renderer.RenderType;

@Mixin(targets = "net.createmod.catnip.impl.client.render.model.UniversalMeshEmitter")
public interface UniversalMeshEmitterAccessor {
    @Invoker("prepare")
    void sporran$invokePrepare(ShadeSeparatedBufferSource bufferSource, RenderType layer);
}
