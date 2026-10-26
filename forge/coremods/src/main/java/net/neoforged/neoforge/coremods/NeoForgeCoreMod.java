/*
 * Copyright (c) NeoForged and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on NeoForge by NeoForged, as ported by KiltMC (see VENDORED.md). Licensed under the GNU LGPL 2.1.
 */

package net.neoforged.neoforge.coremods;

import cpw.mods.modlauncher.api.ITransformer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.neoforged.neoforgespi.coremod.ICoreMod;

public class NeoForgeCoreMod implements ICoreMod {
    @Override
    public Iterable<? extends ITransformer<?>> getTransformers() {
        List<ITransformer<?>> transformers = new ArrayList<>();
        transformers.add(new ReplaceFieldWithGetterAccess("net.minecraft.world.level.biome.Biome", Map.of(
                "climateSettings", "getModifiedClimateSettings",
                "specialEffects", "getModifiedSpecialEffects")));
        transformers.add(new ReplaceFieldWithGetterAccess("net.minecraft.world.level.levelgen.structure.Structure", Map.of(
                "settings", "getModifiedStructureSettings")));
        // Sporran: Disabled due to stack overflow caused by FlowerPotInject::sporran$deferredContentGet
        //transformers.add(new ReplaceFieldWithGetterAccess("net.minecraft.world.level.block.FlowerPotBlock", Map.of(
        //        "potted", "getPotted")));

        transformers.add(new MethodRedirector());

        return transformers;
    }
}
