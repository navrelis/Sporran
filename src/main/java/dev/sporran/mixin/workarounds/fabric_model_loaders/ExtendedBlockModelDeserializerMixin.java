package dev.sporran.mixin.workarounds.fabric_model_loaders;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.fabricmc.loader.api.FabricLoader;
import net.neoforged.neoforge.client.model.ExtendedBlockModelDeserializer;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import dev.sporran.Sporran;

import net.minecraft.resources.ResourceLocation;

/**
 * Sporran: block models of Fabric mods can name a {@code "loader"} that the mod registers through Fabric API's model
 * loading (Moonlight: {@code supplementaries:*}, {@code amendments:*}; Framework; Fast Paintings). NeoForge's
 * deserializer doesn't know those, returns no geometry and lets vanilla/Fabric API load the model as usual, which works.
 * Logging that as an ERROR for every such loader is misleading, so it is only logged at debug level when the loader's
 * namespace belongs to a loaded Fabric mod. Missing loaders of NeoForge mods are still reported as errors.
 */
@Mixin(value = ExtendedBlockModelDeserializer.class, remap = false)
public abstract class ExtendedBlockModelDeserializerMixin {
    @WrapWithCondition(
        method = "deserializeGeometry",
        at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", remap = false),
        remap = false,
        require = 0
    )
    private static boolean sporran$quietForFabricModLoaders(Logger logger, String message, Object loaderName, Object loaders) {
        if (loaderName instanceof ResourceLocation name) {
            var container = FabricLoader.getInstance().getModContainer(name.getNamespace()).orElse(null);
            if (container != null && "fabric".equals(container.getMetadata().getType())) {
                Sporran.Companion.getLogger().debug("Model loader '{}' is not a NeoForge or Porting Lib loader, leaving it to Fabric mod {}", name, name.getNamespace());
                return false;
            }
        }

        return true;
    }
}
