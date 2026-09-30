package dev.sporran.mixin.compat.porting_lib;

import java.util.Map;
import java.util.Optional;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import io.github.fabricators_of_create.porting_lib.loot.LootModifierManager;
import io.github.fabricators_of_create.porting_lib.loot.PortingLibLoot;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LootModifierManager.class)
public abstract class LootModifierManagerMixin {
    @WrapOperation(method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Ljava/util/Map;", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
    private <K, V> V sporran$porting_lib$avoidTempadCrash(Map<K, V> instance, K k, V v, Operation<V> original) {
        // Sporran: Otherwise Tempad causes problems, we have to make sure the elements actually exist.
        if (v == null) {
            return null;
        }

        return original.call(instance, k, v);
    }

    /**
     * Sporran: Porting Lib decodes every global loot modifier in {@code data/&#42;/loot_modifiers} with its own serializer
     * registry, and logs a warning for every modifier whose {@code type} is a NeoForge serializer (like
     * {@code neoforge:add_table}) because it has never heard of it. NeoForge's own manager (Sporran's) still applies
     * those modifiers correctly, so this makes Porting Lib skip them quietly.
     * <p>
     * Only modifiers whose type is registered in NeoForge's registry and NOT in Porting Lib's are skipped, so
     * Fabric mods' modifiers still load, and unknown types still get reported.
     * <p>
     * The method is selected by name only ({@code apply} in the mod's own namespace, the vanilla override is
     * {@code method_18788}) and is optional ({@code require = 0}), so a Porting Lib update can't crash the game.
     */
    @WrapOperation(
        method = "apply",
        at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/Codec;parse(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;", remap = false),
        require = 0,
        expect = 0,
        remap = false
    )
    @SuppressWarnings("unchecked")
    private <A, T> DataResult<A> sporran$porting_lib$skipNeoForgeModifiers(Codec<A> codec, DynamicOps<T> ops, T input, Operation<DataResult<A>> original) {
        if (sporran$porting_lib$isNeoForgeOnlyModifier(input)) {
            // The codec is Codec<Optional<WithConditions<IGlobalLootModifier>>>, where an empty Optional means
            // "not loaded" (for example, because its conditions weren't met). That's exactly what we want here.
            return DataResult.success((A) Optional.empty());
        }

        return original.call(codec, ops, input);
    }

    @Unique
    private static boolean sporran$porting_lib$isNeoForgeOnlyModifier(Object input) {
        try {
            if (!(input instanceof JsonObject json)) {
                return false;
            }

            JsonElement typeElement = json.get("type");
            if (typeElement == null || !typeElement.isJsonPrimitive() || !typeElement.getAsJsonPrimitive().isString()) {
                return false;
            }

            ResourceLocation type = ResourceLocation.tryParse(typeElement.getAsString());
            if (type == null || !NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS.containsKey(type)) {
                return false;
            }

            // If Porting Lib knows this type itself (a Fabric mod's modifier), leave it alone.
            return !PortingLibLoot.GLOBAL_LOOT_MODIFIER_SERIALIZERS.containsKey(type);
        } catch (Throwable ignored) {
            // Whatever goes wrong here (including a Porting Lib update renaming that field), fall back to
            // Porting Lib's normal behaviour.
            return false;
        }
    }
}
