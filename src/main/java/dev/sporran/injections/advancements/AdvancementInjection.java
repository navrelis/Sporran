/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.advancements;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.Advancement;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.WithConditions;

import java.util.Optional;

public interface AdvancementInjection {
    Codec<Optional<WithConditions<Advancement>>> CONDITIONAL_CODEC = ConditionalOps.createConditionalCodecWithConditions(Advancement.CODEC);
}
