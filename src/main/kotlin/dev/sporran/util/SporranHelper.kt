package dev.sporran.util

import cpw.mods.util.Lazy
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.impl.launch.FabricLauncherBase
import net.neoforged.fml.ModLoadingContext
import org.objectweb.asm.ClassReader
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import dev.sporran.Sporran
import dev.sporran.loader.SporranFlags
import dev.sporran.loader.SporranLoader
import java.io.File
import java.lang.reflect.Modifier
import java.nio.file.Path
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.jar.JarFile

object SporranHelper {
    val launcher = FabricLauncherBase.getLauncher()

    private val cachedForgeClassNodes = Lazy.of { getForgeClassNodesInternal() }
    private var isForgeClassNodesCleared = false

    // normally I'd expect a synchronized map to be faster, but in our case I guess concurrent hash map is faster?
    private val cachedClassValues: MutableMap<OverrideData, ClassValue<Boolean>> = ConcurrentHashMap()

    /**
     * Sporran: [hasMethodOverride] compares [java.lang.reflect.Method.getName] against a string. Loom remaps method
     * references but never string literals, so a vanilla (Mojang) method name only matches in the dev environment;
     * in production the runtime name is the intermediary one (e.g. `method_11017`) and the check is always false.
     * Use this to get the runtime name of a *vanilla* method from its intermediary owner/name/descriptor.
     * Methods that NeoForge added are not part of the mappings and keep their plain string literal.
     */
    @JvmStatic
    fun mapVanillaMethodName(intermediaryOwner: String, intermediaryName: String, intermediaryDescriptor: String): String {
        return FabricLoader.getInstance().mappingResolver.mapMethodName("intermediary", intermediaryOwner, intermediaryName, intermediaryDescriptor)
    }

    // BlockEntity#getType, net.minecraft.class_2586 / method_11017 / ()Lnet/minecraft/class_2591;
    @JvmField
    val BLOCK_ENTITY_GET_TYPE: String = mapVanillaMethodName("net.minecraft.class_2586", "method_11017", "()Lnet/minecraft/class_2591;")

    // Entity#getType, net.minecraft.class_1297 / method_5864 / ()Lnet/minecraft/class_1299;
    @JvmField
    val ENTITY_GET_TYPE: String = mapVanillaMethodName("net.minecraft.class_1297", "method_5864", "()Lnet/minecraft/class_1299;")

    // LevelReader#isEmptyBlock, net.minecraft.class_4538 / method_22347 / (Lnet/minecraft/class_2338;)Z
    @JvmField
    val LEVEL_READER_IS_EMPTY_BLOCK: String = mapVanillaMethodName("net.minecraft.class_4538", "method_22347", "(Lnet/minecraft/class_2338;)Z")

    private fun checkAllElementsMatch(array: Array<*>, array2: Array<*>): Boolean {
        if (array.size != array2.size)
            return false

        for ((i, first) in array.withIndex()) {
            if (first != array2[i])
                return false
        }

        return true
    }

    fun hasMethodOverride(topClass: Class<*>, superClass: Class<*>, methodName: String, vararg methodArgs: Class<*>): Boolean {
        return getOverrideClassValue(OverrideData(superClass, methodName, methodArgs)).get(topClass)
    }

    fun hasMethodOverrideWithReturnType(topClass: Class<*>, superClass: Class<*>, methodName: String, returnType: Class<*>, vararg methodArgs: Class<*>): Boolean {
        return getOverrideClassValue(OverrideData(superClass, methodName, methodArgs, returnType)).get(topClass)
    }

    // Sporran: look the ClassValue up before computeIfAbsent, whose lambda captured the arguments and so was allocated on
    //  every call, even though the key is almost always cached already.
    private fun getOverrideClassValue(overrideData: OverrideData): ClassValue<Boolean> {
        return this.cachedClassValues[overrideData] ?: this.cachedClassValues.computeIfAbsent(overrideData, ::createOverrideClassValue)
    }

    private fun createOverrideClassValue(overrideData: OverrideData): ClassValue<Boolean> {
        val superClass = overrideData.superClass
        val methodName = overrideData.methodName
        val methodArgs = overrideData.methodArgs
        val returnType = overrideData.returnType

        if (returnType == null) {
            return object : ClassValue<Boolean>() {
                override fun computeValue(type: Class<*>): Boolean {
                    try {
                        val method = type.getMethod(methodName, *methodArgs)

                        // If the method is declared in the superClass itself (e.g., default interface method),
                        // it's not an override - return false
                        if (method.declaringClass == superClass) {
                            return false
                        }

                        // If a Fabric mod has this method signature but is private, it could cause a game crash.
                        return !Modifier.isPrivate(method.modifiers)
                    } catch (_: Throwable) {
                        return false
                    }
                }
            }
        }

        return object : ClassValue<Boolean>() {
            override fun computeValue(type: Class<*>): Boolean {
                try {
                    val methods = type.methods.filter { it.name == methodName && it.parameterTypes.contentEquals(methodArgs) && it.returnType == returnType }

                    if (methods.isEmpty())
                        return false

                    val method = methods.first()

                    // If the method is declared in the superClass itself (e.g., default interface method),
                    // it's not an override - return false
                    if (method.declaringClass == superClass) {
                        return false
                    }

                    // If a Fabric mod has this method signature but is private, it could cause a game crash.
                    return !Modifier.isPrivate(method.modifiers)
                } catch (_: Throwable) {
                    return false
                }
            }
        }
    }

    /**
     * Sporran: a [hasMethodOverride] check for hot paths (several calls per entity per tick, e.g. pathfinding and fluid
     * lookups). Keep it in a static field; [test] then only costs a [ClassValue] lookup, while [hasMethodOverride]
     * allocates its varargs and a cache key on every call. It shares the cache of [hasMethodOverride], so the result is
     * always the same, and it resolves lazily so that creating one in a static initializer doesn't touch [SporranHelper].
     */
    class MethodOverrideCheck private constructor(private val overrideData: OverrideData) {
        @Volatile
        private var classValue: ClassValue<Boolean>? = null

        fun test(topClass: Class<*>): Boolean {
            val classValue = this.classValue ?: SporranHelper.getOverrideClassValue(overrideData).also { this.classValue = it }
            return classValue.get(topClass)
        }

        companion object {
            /** Same as [hasMethodOverride]. */
            @JvmStatic
            fun of(superClass: Class<*>, methodName: String, vararg methodArgs: Class<*>): MethodOverrideCheck {
                return MethodOverrideCheck(OverrideData(superClass, methodName, methodArgs))
            }

            /** Same as [hasMethodOverrideWithReturnType]. */
            @JvmStatic
            fun withReturnType(superClass: Class<*>, methodName: String, returnType: Class<*>, vararg methodArgs: Class<*>): MethodOverrideCheck {
                return MethodOverrideCheck(OverrideData(superClass, methodName, methodArgs, returnType))
            }
        }
    }

    fun getForgeClassNodes(): List<ClassNode> {
        if (isForgeClassNodesCleared) {
            throw IllegalStateException("NeoForge class nodes have already been cleared!")
        }

        return cachedForgeClassNodes.get()
    }

    fun clearForgeClassNodes() {
        cachedForgeClassNodes.get().clear()
        isForgeClassNodesCleared = true
    }

    fun joinToString(array: Array<String>, separator: String): String {
        return array.joinToString(separator)
    }

    fun <E> mergeNullableCollections(vararg collections: Collection<E>?): Collection<E> {
        val merged = mutableListOf<E>()

        for (collection in collections) {
            if (collection != null) {
                merged.addAll(collection)
            }
        }

        return merged
    }

    fun getSporranPaths(): List<Path> {
        return if (!FabricLoader.getInstance().isDevelopmentEnvironment) {
            //listOf(SporranLoader::class.java.protectionDomain.codeSource.location.toURI().toPath())
            listOf()
        } else {
            val filesToScan = mutableListOf<Path>()

            // Main environment
            run {
                filesToScan.add(getPath("dev/sporran/loader/SporranLoader.class") ?: return@run)
                filesToScan.add(getPath("net/neoforged/neoforge/common/NeoForgeMod.class") ?: return@run)
            }

            // Test environment
            run {
                filesToScan.add(getPath("dev/sporran/test/SporranTesting.class") ?: return@run)
                // Sporran TODO: fix
//                filesToScan.add(getPath("net/minecraftforge/test/LazyOptionalTest.class") ?: return@run)
            }

            filesToScan
        }
    }

    private fun getPath(path: String): Path? {
        val classUrl = launcher.targetClassLoader.getResource(path) ?: return null
        val fullPath = classUrl.path.replace("/$path", "")

        return File(fullPath).toPath()
    }

    // Sporran: the NeoForge class nodes are only used for their names, outer classes and annotations (SporranEarlyRiser and
    //  the development scan data), so skip the method bodies of the whole translator jar.
    private const val FORGE_CLASS_NODE_PARSING_OPTIONS = ClassReader.SKIP_CODE or ClassReader.SKIP_DEBUG or ClassReader.SKIP_FRAMES

    private fun getForgeClassNodesInternal(): MutableList<ClassNode> {
        val list = mutableListOf<ClassNode>()

        if (!FabricLoader.getInstance().isDevelopmentEnvironment) {
            val sporranFile = File(SporranLoader::class.java.protectionDomain.codeSource.location.toURI())
            val sporranJar = JarFile(sporranFile)

            sporranJar.entries().asIterator().forEach {
                if (it.name.endsWith(".class")) {
                    val inputStream = sporranJar.getInputStream(it)
                    val classReader = ClassReader(inputStream)
                    val classNode = ClassNode(Opcodes.ASM9)
                    classReader.accept(classNode, FORGE_CLASS_NODE_PARSING_OPTIONS)

                    list.add(classNode)
                }
            }
        } else {
            // Need to do this workaround to scan the Sporran JAR in dev.

            val filesToScan = getSporranPaths()

            filesToScan.forEach { file ->
                file.toFile().walk().forEach {
                    if (it.name.endsWith(".class")) {
                        val inputStream = it.inputStream()
                        val classReader = ClassReader(inputStream)
                        val classNode = ClassNode(Opcodes.ASM9)
                        classReader.accept(classNode, FORGE_CLASS_NODE_PARSING_OPTIONS)

                        list.add(classNode)
                    }
                }
            }
        }

        return list
    }

    @JvmRecord
    internal data class OverrideData(
        val superClass: Class<*>,
        val methodName: String,
        val methodArgs: Array<out Class<*>>,
        val returnType: Class<*>? = null
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as OverrideData

            if (superClass != other.superClass) return false
            if (methodName != other.methodName) return false
            if (!methodArgs.contentEquals(other.methodArgs)) return false
            // Sporran: include the return type, otherwise a plain check and a return type check of the same method shared
            //  one cache entry, and whichever was created first decided the result of both.
            if (returnType != other.returnType) return false

            return true
        }

        override fun hashCode(): Int {
            var result = superClass.hashCode()
            result = 31 * result + methodName.hashCode()
            result = 31 * result + methodArgs.contentHashCode()
            result = 31 * result + returnType.hashCode()
            return result
        }
    }

    // Turns out, there are Forge mods that forcibly exit the game if Sporran or Porting Lib are detected, with zero information provided whatsoever.
    // I don't exactly *want* to argue nor combat this, but because of how hostile this act is, the fact that it directly hinders Sporran development,
    // that the users are provided with zero information as to why their game closed, that if Sporran grows popular enough these mods will not be able
    // to be used alongside Sporran whatsoever, and any further reasons, I kind of have to do this, as a compromise between the mod developer's wishes
    // and me trying to make Sporran even better.
    // See: https://discord.com/channels/1062715218087645215/1373601947155693588, https://github.com/KiltMC/Kilt/issues/75
    @JvmStatic
    fun handleSystemExit(code: Int) {
        Sporran.logger.error("Sporran: A Forge mod has called System.exit() directly from its code! Exit code: $code")

        if (ModLoadingContext.get().activeContainer != null) {
            val container = ModLoadingContext.get().activeContainer
            Sporran.logger.error("Sporran: Active mod container: ${container.modInfo.displayName} (${container.modId})")
        }

        val exception = IllegalStateException("A Forge mod has called System.exit($code) directly! Please check the logs, and report this to the Sporran issues page!")

        if (FabricLoader.getInstance().isDevelopmentEnvironment) {
            exception.printStackTrace()
            Sporran.logger.error("Sporran: Because we're in the development environment, we will not exit the game!")

            return
        } else if (SporranFlags.DISABLE_FORGE_SYSTEM_EXIT) {
            exception.printStackTrace()
            Sporran.logger.error("Sporran: Because the user has the disable flag enabled, we will not exit the game!")

            return
        }

        throw exception
    }

    @JvmStatic
    fun createMixinException(injection: Class<*>, methodName: String): RuntimeException {
        return RuntimeException("Sporran: ${injection.simpleName}#$methodName is not implemented")
    }

    @JvmStatic
    fun resolveModuleToUnnamed(module: Optional<Module>): Optional<Module> {
        if (module.isPresent)
            return module

        return Optional.of(this::class.java.module)
    }
}
