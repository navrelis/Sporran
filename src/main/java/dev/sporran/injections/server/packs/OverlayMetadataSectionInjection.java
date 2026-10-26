/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.server.packs;

import net.minecraft.server.packs.OverlayMetadataSection;
import net.minecraft.server.packs.metadata.MetadataSectionType;

public interface OverlayMetadataSectionInjection {
    public static final MetadataSectionType<OverlayMetadataSection> NEOFORGE_TYPE = MetadataSectionType.fromCodec("neoforge:overlays", OverlayMetadataSection.CODEC);
}
