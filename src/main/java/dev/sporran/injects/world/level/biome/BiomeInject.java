// TRACKED HASH: 65326a80010482966a2797124eab63fbaf21f71e
package dev.sporran.injects.world.level.biome;

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

    @Inject(at = @At("HEAD"), method = "getMobSettings", cancellable = true)
    public void sporran$getModifiableMobSettings(CallbackInfoReturnable<MobSpawnSettings> cir) {
        cir.setReturnValue(modifiableBiomeInfo().get().mobSpawnSettings());
    }

    @Inject(at = @At("HEAD"), method = "getGenerationSettings", cancellable = true)
    public void sporran$getModifiableGenerationSettings(CallbackInfoReturnable<BiomeGenerationSettings> cir) {
        cir.setReturnValue(modifiableBiomeInfo().get().generationSettings());
    }
}
