package dev.sporran.mixin.compat.porting_lib;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegistryManager;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Porting Lib ships its own data map loader, which reads the same {@code data/&#42;/data_maps/} folders that
 * NeoForge's (Sporran's) real data map loader reads. Porting Lib only knows about its own data map types, so it
 * logs "Found data map file for non-existent data map type" for every NeoForge data map, even though those are
 * loaded properly by NeoForge's loader.
 * <p>
 * This only drops that warning when the type is a registered NeoForge data map, so typos and genuinely unknown
 * types still get reported.
 * <p>
 * The warning lives in a synthetic lambda whose number changes between Porting Lib builds, so the method is
 * selected with a regex, and every injector is optional ({@code require = 0}) so that a Porting Lib update can
 * never crash the game because of this mixin.
 */
@Pseudo
@Mixin(targets = "io.github.fabricators_of_create.porting_lib.resources.data_maps.DataMapLoader", remap = false)
public abstract class PortingLibDataMapLoaderMixin {
    @WrapWithCondition(
        method = "/^lambda\\$load\\$\\d+$/",
        at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", remap = false),
        require = 0,
        expect = 0,
        remap = false
    )
    private static boolean sporran$skipWarningForNeoForgeDataMaps(Logger logger, String message, Object dataMapId, Object registryId) {
        try {
            if (dataMapId instanceof ResourceLocation id && registryId instanceof ResourceLocation registry
                && RegistryManager.getDataMap(ResourceKey.createRegistryKey(registry), id) != null) {
                return false;
            }
        } catch (Throwable ignored) {
            // Never let this break Porting Lib's own logging.
        }

        return true;
    }
}
