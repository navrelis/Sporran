// TRACKED HASH: 65326a80010482966a2797124eab63fbaf21f71e
/*
 * Changed by the Sporran project between 2026-09-29 and 2026-10-27; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.level.biome;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import dev.sporran.injections.world.level.biome.BiomeInjection;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;

@Mixin(Biome.class)
public abstract class BiomeInject implements BiomeInjection {
    private ModifiableBiomeInfo modifiableBiomeInfo;

    @Inject(at = @At("TAIL"), method = "<init>")
    public void sporran$loadModifiableBiomeInfo(Biome.ClimateSettings climateSettings, BiomeSpecialEffects biomeSpecialEffects, BiomeGenerationSettings biomeGenerationSettings, MobSpawnSettings mobSpawnSettings, CallbackInfo ci) {
        this.modifiableBiomeInfo = new ModifiableBiomeInfo(new ModifiableBiomeInfo.BiomeInfo(climateSettings, biomeSpecialEffects, biomeGenerationSettings, mobSpawnSettings));
    }

    @Override
    public ModifiableBiomeInfo modifiableBiomeInfo() {
        return this.modifiableBiomeInfo;
    }

    @Override
    public BiomeSpecialEffects getModifiedSpecialEffects() {
        return modifiableBiomeInfo().get().effects();
    }

    @Override
    public Biome.ClimateSettings getModifiedClimateSettings() {
        return modifiableBiomeInfo().get().climateSettings();
    }

    @Inject(at = @At("HEAD"), method = "method_28423", cancellable = true)
    private static void sporran$useOriginalClimateSettings(Biome biome, CallbackInfoReturnable<Biome.ClimateSettings> cir) {
        cir.setReturnValue(biome.modifiableBiomeInfo().getOriginalBiomeInfo().climateSettings());
    }

    @Inject(at = @At("HEAD"), method = "method_28421", cancellable = true)
    private static void sporran$useOriginalSpecialEffects(Biome biome, CallbackInfoReturnable<BiomeSpecialEffects> cir) {
        cir.setReturnValue(biome.modifiableBiomeInfo().getOriginalBiomeInfo().effects());
    }

    // Sporran: these replaced the vanilla getters at HEAD, so another mod's mixin into them never ran. The vanilla body
    //  returns the original settings; swap in the biome-modifier result only when nothing else changed the value.
    @ModifyReturnValue(at = @At("RETURN"), method = "getMobSettings")
    public MobSpawnSettings sporran$getModifiableMobSettings(MobSpawnSettings original) {
        return original == modifiableBiomeInfo().getOriginalBiomeInfo().mobSpawnSettings() ? modifiableBiomeInfo().get().mobSpawnSettings() : original;
    }

    @ModifyReturnValue(at = @At("RETURN"), method = "getGenerationSettings")
    public BiomeGenerationSettings sporran$getModifiableGenerationSettings(BiomeGenerationSettings original) {
        return original == modifiableBiomeInfo().getOriginalBiomeInfo().generationSettings() ? modifiableBiomeInfo().get().generationSettings() : original;
    }
}
