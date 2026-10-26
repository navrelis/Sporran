/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.level.levelgen;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.level.levelgen.WorldDimensions;

@Mixin(WorldDimensions.class)
public abstract class WorldDimensionsInject {
    // Sporran TODO: do we want to do this?
    /*@Redirect(method = "method_45516", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/Codec;unboundedMap(Lcom/mojang/serialization/Codec;Lcom/mojang/serialization/Codec;)Lcom/mojang/serialization/codecs/UnboundedMapCodec;"))
    private static <K, V> UnboundedMapCodec<K, V> sporran$useLenientCodec(Codec<K> keyCodec, Codec<V> elementCodec) {
        return new LenientUnboundedMapCodec<>(keyCodec, elementCodec);
    }*/
}
