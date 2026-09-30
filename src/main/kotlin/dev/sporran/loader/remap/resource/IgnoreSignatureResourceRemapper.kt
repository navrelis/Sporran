package dev.sporran.loader.remap.resource

import dev.sporran.loader.remap.SporranRemapper
import xyz.bluspring.knit.loader.mod.ModDefinition
import java.io.InputStream

object IgnoreSignatureResourceRemapper : ResourceRemapper {
    override fun canTransform(path: String): Boolean {
        return path.lowercase().endsWith(".rsa") || path.lowercase().endsWith(".sf")
    }

    override fun transform(mod: ModDefinition, path: String, input: InputStream): ByteArray? {
        // ignore JAR signatures.
        // Due to Sporran remapping the JAR files, we are unable to use this to our advantage.
        // TODO: Maybe run a verification step in the mod loading process prior to remapping?
        SporranRemapper.logger.warn("Detected that ${mod.displayName} (${mod.id}) is a signed JAR! This is a security measure by mod developers to verify that the distributed mod JARs are theirs, however Sporran is unable to use this verification step properly, and is thus stripping this information.")

        return null
    }
}