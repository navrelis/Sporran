// TRACKED HASH: c3fbda8033be117044af2f7df8b9938868361aaa
/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.server.packs.metadata.pack;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.util.InclusiveRange;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dev.sporran.helpers.mixin.CreateInitializer;
import dev.sporran.injections.server.packs.metadata.pack.PackMetadataSectionInjection;

import java.util.Map;
import java.util.Optional;

@Mixin(PackMetadataSection.class)
public abstract class PackMetadataSectionInject implements PackMetadataSectionInjection {
    public PackMetadataSectionInject(Component description, int packFormat, Optional<InclusiveRange<Integer>> supportedFormats) {
    }

    @CreateInitializer
    public PackMetadataSectionInject(Component description, int packFormat) {
        this(description, packFormat, Optional.empty());
    }
}