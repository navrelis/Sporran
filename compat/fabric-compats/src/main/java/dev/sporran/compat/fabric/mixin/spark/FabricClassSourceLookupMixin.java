package dev.sporran.compat.fabric.mixin.spark;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import dev.sporran.util.ModRootPathKeys;
import net.fabricmc.loader.api.ModContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Sporran: spark builds its path to mod id map with a Guava {@code ImmutableMap}, whose builder throws on a repeated
 * key. The {@code neoforge} container that Sporran adds has the same root path (the Sporran jar) as Sporran itself, so
 * the map construction failed with "Multiple entries with same key" and spark lost its per-mod "Sources" view. The root
 * paths whose key an earlier container already used are left out, so the first container keeps the path.
 */
@IfModLoaded("spark")
@Pseudo
@Mixin(targets = "me.lucko.spark.fabric.FabricClassSourceLookup", remap = false)
public abstract class FabricClassSourceLookupMixin {
    /** The keys seen by the map construction that is running on this thread; null outside of one. */
    @Unique private static final ThreadLocal<ModRootPathKeys> sporran$keys = new ThreadLocal<>();

    @WrapMethod(method = "constructPathToModIdMap", require = 0)
    private static Map<String, String> sporran$spark$trackRootPathKeys(Collection<ModContainer> mods, Operation<Map<String, String>> original) {
        ModRootPathKeys previous = sporran$keys.get();
        sporran$keys.set(new ModRootPathKeys());

        try {
            return original.call(mods);
        } finally {
            if (previous == null) {
                sporran$keys.remove();
            } else {
                sporran$keys.set(previous);
            }
        }
    }

    @WrapOperation(
            method = "constructPathToModIdMap",
            at = @At(value = "INVOKE", target = "Lnet/fabricmc/loader/api/ModContainer;getRootPaths()Ljava/util/List;"),
            require = 0
    )
    private static List<Path> sporran$spark$skipDuplicateRootPaths(ModContainer container, Operation<List<Path>> original) {
        List<Path> paths = original.call(container);
        ModRootPathKeys keys = sporran$keys.get();

        return keys == null ? paths : keys.removeSeen(paths);
    }
}
