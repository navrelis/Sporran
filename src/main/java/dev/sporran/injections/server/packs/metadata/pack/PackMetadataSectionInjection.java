/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.server.packs.metadata.pack;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;

import java.util.Map;
import java.util.Optional;

public interface PackMetadataSectionInjection {
    static PackMetadataSection create(Component description, int packFormat) {
        return new PackMetadataSection(description, packFormat, Optional.empty());
    }
}
