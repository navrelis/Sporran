package dev.sporran.loader.asm

import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.ModContainer
import net.fabricmc.loader.impl.launch.FabricLauncherBase
import org.objectweb.asm.ClassReader
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import xyz.bluspring.fork.mm.api.ClassTinkerers
import dev.sporran.Sporran
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.extension
import kotlin.io.path.invariantSeparatorsPathString
import kotlin.io.path.isDirectory
import kotlin.io.path.readBytes

/**
 * Sporran: some Fabric mods bundle their own copies of NeoForge API classes under the same names Sporran ships
 * (for example NeoForge Data Pack Extensions, nested in Dyed Flames, has its own `net.neoforged.neoforge.registries.*`).
 * Knot looks classes up in the order of the mod IDs, so a mod whose ID sorts before `sporran` wins, and Sporran's own
 * NeoForge code then runs against a foreign, incompatible copy (NoSuchMethodError in `NeoForgeRegistries.<clinit>`).
 *
 * For every class that such a mod would provide instead of Sporran, the class is replaced with Sporran's own bytes
 * before any mixin is applied to it, the same way SporranEarlyRiser already replaces Forge Config API Port's config classes.
 * Forge Config API Port itself is left to that hand-picked list (Sporran's config support builds on the rest of it).
 */
object ForeignNeoForgeClasses {
    private val ALLOWED_MOD_IDS = setOf("forgeconfigapiport")
    private const val PREFIX = "net/neoforged/"

    fun replaceForeignCopies(alreadyReplaced: Set<String>) {
        val loader = FabricLoader.getInstance()
        val sporran = loader.getModContainer(Sporran.MOD_ID).orElse(null) ?: return
        val launcher = FabricLauncherBase.getLauncher()
        val classLoader = launcher.targetClassLoader
        val replaced = sortedMapOf<String, MutableSet<String>>()

        for (container in loader.allMods) {
            // NeoForge mods that Knit registers with Fabric Loader (type "neoforge") are not Fabric mods bundling copies.
            if (container.metadata.id == Sporran.MOD_ID || container.metadata.type == "neoforge" || container.metadata.id in ALLOWED_MOD_IDS || isInsideSporran(container))
                continue

            for (root in container.rootPaths) {
                val neoRoot = root.resolve(PREFIX)
                if (!neoRoot.isDirectory())
                    continue

                Files.walk(neoRoot).use { stream ->
                    stream.filter { it.extension == "class" }.forEach { path ->
                        val fileName = PREFIX + neoRoot.relativize(path).invariantSeparatorsPathString
                        val sporranPath = sporran.findPath(fileName).orElse(null) ?: return@forEach

                        // Only when the class the lookup would load differs from Sporran's (identical copies are fine).
                        val url = classLoader.getResource(fileName) ?: return@forEach
                        val found = runCatching { url.openStream().use { it.readBytes() } }.getOrNull() ?: return@forEach
                        if (found.contentEquals(sporranPath.readBytes()))
                            return@forEach

                        val className = fileName.removeSuffix(".class")
                        if (className in alreadyReplaced || replaced.values.any { className in it })
                            return@forEach

                        // Classes that are already loaded (fml.common.Mod, used while Sporran scans the NeoForge mods)
                        // can't be a mixin target any more.
                        if (launcher.isClassLoaded(className.replace('/', '.'))) {
                            if (members(found) != members(sporranPath.readBytes()))
                                Sporran.logger.warn("Sporran: Fabric mod ${container.metadata.id} bundles a different copy of ${className.replace('/', '.')}, which was already loaded and can't be replaced with Sporran's")
                            return@forEach
                        }

                        replaced.getOrPut(container.metadata.id) { sortedSetOf() }.add(className)
                        ClassTinkerers.addReplacement(className) { node -> replaceWith(node, sporranPath) }
                    }
                }
            }
        }

        for ((modId, classes) in replaced) {
            Sporran.logger.info("Sporran: Fabric mod $modId bundles ${classes.size} NeoForge classes that Sporran provides itself, Sporran's own versions are used: ${classes.joinToString(", ") { it.substringAfterLast('/') }}")
        }
    }

    private fun isInsideSporran(container: ModContainer): Boolean {
        var parent = container.containingMod.orElse(null)
        while (parent != null) {
            if (parent.metadata.id == Sporran.MOD_ID)
                return true
            parent = parent.containingMod.orElse(null)
        }
        return false
    }

    private fun members(bytes: ByteArray): Set<String> {
        val node = ClassNode(Opcodes.ASM9)
        ClassReader(bytes).accept(node, ClassReader.SKIP_CODE or ClassReader.SKIP_DEBUG or ClassReader.SKIP_FRAMES)
        return (node.fields.map { "${it.name}:${it.desc}" } + node.methods.map { "${it.name}${it.desc}" }).toSet() + (node.superName ?: "") + node.interfaces
    }

    private fun replaceWith(node: ClassNode, sporranPath: Path) {
        if (!sporranPath.exists())
            return

        val own = ClassNode(Opcodes.ASM9)
        ClassReader(sporranPath.readBytes()).accept(own, 0)

        node.version = own.version
        node.access = own.access
        node.name = own.name
        node.signature = own.signature
        node.superName = own.superName
        node.interfaces = own.interfaces
        node.sourceFile = own.sourceFile
        node.sourceDebug = own.sourceDebug
        node.module = own.module
        node.outerClass = own.outerClass
        node.outerMethod = own.outerMethod
        node.outerMethodDesc = own.outerMethodDesc
        node.visibleAnnotations = own.visibleAnnotations
        node.invisibleAnnotations = own.invisibleAnnotations
        node.visibleTypeAnnotations = own.visibleTypeAnnotations
        node.invisibleTypeAnnotations = own.invisibleTypeAnnotations
        node.attrs = own.attrs
        node.innerClasses = own.innerClasses
        node.nestHostClass = own.nestHostClass
        node.nestMembers = own.nestMembers
        node.permittedSubclasses = own.permittedSubclasses
        node.recordComponents = own.recordComponents
        node.fields = own.fields
        node.methods = own.methods
    }
}
