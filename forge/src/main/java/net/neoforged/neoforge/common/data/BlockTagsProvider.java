/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on NeoForge by NeoForged, as ported by KiltMC (see VENDORED.md). Licensed under the GNU LGPL 2.1.
 */

package net.neoforged.neoforge.common.data;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public abstract class BlockTagsProvider extends IntrinsicHolderTagsProvider<Block> {
    @SuppressWarnings("deprecation")
    public BlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.BLOCK, lookupProvider, block -> block.builtInRegistryHolder().key()/*, modId, existingFileHelper*/);
        this.sporran$setModId(modId);
        this.sporran$setExistingFileHelper(existingFileHelper);
    }
}
