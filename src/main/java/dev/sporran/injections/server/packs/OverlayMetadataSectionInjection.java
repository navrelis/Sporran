/*
 * Changed by the Sporran project between 2026-09-29 and 2026-10-26; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.server.packs;

import com.mojang.serialization.Codec;
import net.minecraft.server.packs.OverlayMetadataSection;
import net.minecraft.server.packs.metadata.MetadataSectionType;

import java.util.List;

public interface OverlayMetadataSectionInjection {
    public static final MetadataSectionType<OverlayMetadataSection> NEOFORGE_TYPE = MetadataSectionType.fromCodec("neoforge:overlays", OverlayMetadataSection.CODEC);

    /**
     * Only tells whether a pack declares Fabric API's {@code fabric:overlays} section; the content is never looked at,
     * so the codec accepts any JSON and cannot fail (a failed decode would make the section look absent).
     */
    public static final MetadataSectionType<?> FABRIC_TYPE = MetadataSectionType.fromCodec("fabric:overlays", Codec.PASSTHROUGH);

    /**
     * Which {@code neoforge:overlays} directories of a pack Sporran adds to its overlay list. A pack that also declares
     * {@code fabric:overlays} (a Fabric mod that ships both, like Lithostitched) has its overlays decided by Fabric API
     * with the conditions the author wrote for Fabric. Its NeoForge conditions would be decoded by Sporran, where a
     * condition that is not a NeoForge one falls back to "true", and overlays the author switched off would be applied.
     */
    public static List<String> neoForgeOverlaysToAdd(boolean declaresFabricOverlays, List<String> neoForgeOverlays) {
        return declaresFabricOverlays ? List.of() : neoForgeOverlays;
    }
}
