package dev.sporran.loader

import net.fabricmc.loader.api.FabricLoader
import net.neoforged.neoforgespi.language.IModInfo
import org.apache.maven.artifact.versioning.VersionRange
import dev.sporran.shaded.cichlidmc.tinycodecs.api.codec.Codec
import dev.sporran.shaded.cichlidmc.tinycodecs.api.codec.CompositeCodec
import dev.sporran.util.enumThrowingFallbackCodec
import dev.sporran.util.unboundedMap
import xyz.bluspring.knit.loader.mod.ModDependency.Type
import xyz.bluspring.knit.loader.mod.ModEnvironment
import java.nio.file.Path
import java.util.*

@JvmRecord
data class SporranLoaderConfig(
    /**
     * Represents the list of mod IDs that should not be getting resolved by Sporran.
     */
    val forceDisabledModIds: List<String> = emptyList(),

    /**
     * Dependency overrides, a Sporran alternative to https://wiki.fabricmc.net/tutorial:dependency_overrides
     */
    val dependencyOverrides: Map<String, Map<String, ModDependencyOverride>> = emptyMap()
) {
    companion object {
        val PATH: Path = FabricLoader.getInstance().configDir.resolve("sporran_overrides.json")
        val CODEC: Codec<SporranLoaderConfig> = CompositeCodec.of(
            Codec.STRING.listOf().optional(emptyList())
                .fieldOf("force_disabled_mods"), SporranLoaderConfig::forceDisabledModIds,
            unboundedMap(Codec.STRING, unboundedMap(Codec.STRING, ModDependencyOverride.CODEC))
                .optional(emptyMap()).fieldOf("dependency_overrides"), SporranLoaderConfig::dependencyOverrides,

            ::SporranLoaderConfig
        ).codec
    }

    data class ModDependencyOverride(
        val version: Optional<VersionRange> = Optional.empty(),
        val type: Optional<Type> = Optional.empty(),
        val side: Optional<ModEnvironment> = Optional.empty(),
        val ordering: Optional<IModInfo.Ordering> = Optional.empty()
    ) {
        companion object {
            val VERSION_CONSTRAINT_CODEC: Codec<VersionRange> = Codec.STRING.xmap(
                VersionRange::createFromVersionSpec,
                VersionRange::toString
            )

            val CODEC: Codec<ModDependencyOverride> = CompositeCodec.of(
                VERSION_CONSTRAINT_CODEC.optional().fieldOf("version"), ModDependencyOverride::version,
                enumThrowingFallbackCodec<Type>().optional().fieldOf("type"), ModDependencyOverride::type,
                enumThrowingFallbackCodec<ModEnvironment>().optional().fieldOf("side"), ModDependencyOverride::side,
                enumThrowingFallbackCodec<IModInfo.Ordering>().optional().fieldOf("ordering"), ModDependencyOverride::ordering,
                ::ModDependencyOverride
            ).codec
        }
    }
}
