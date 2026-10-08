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

    fun hasMethodOverride(topClass: Class<*>, superClass: Class<*>, methodName: String, vararg methodArgs: Class<*>): Boolean {
        return MethodOverrideCache.hasMethodOverride(topClass, superClass, methodName, methodArgs, null)
    }

    fun hasMethodOverrideWithReturnType(topClass: Class<*>, superClass: Class<*>, methodName: String, returnType: Class<*>, vararg methodArgs: Class<*>): Boolean {
        return MethodOverrideCache.hasMethodOverride(topClass, superClass, methodName, methodArgs, returnType)
    }

    /**
     * Sporran: the cache behind [hasMethodOverride], [hasMethodOverrideWithReturnType] and [MethodOverrideCheck]. It is an
     * object of its own that uses nothing of Fabric Loader or Minecraft, so a [MethodOverrideCheck] never initialises
     * [SporranHelper], and so it can be unit tested (SporranHelper itself needs a running Fabric Loader).
     */
    internal object MethodOverrideCache {
        // normally I'd expect a synchronized map to be faster, but in our case I guess concurrent hash map is faster?
        private val cachedClassValues: MutableMap<OverrideData, ClassValue<Boolean>> = ConcurrentHashMap()

        // Sporran: hasMethodOverride built an OverrideData key on every call, only to look its ClassValue up in the map
        //  above (hashing and comparing the argument types again each time). This table answers the same lookup straight
        //  from the arguments, without creating anything. It only ever holds entries of cachedClassValues (same key, same
        //  ClassValue), so the result is the same. The array is never changed once it is published: a new key replaces it
        //  with a changed copy, which happens once per distinct check, so readers need no lock.
        @Volatile
        private var overrideLookupTable: Array<OverrideLookupEntry?> = arrayOfNulls(64)
        private var overrideLookupSize = 0
        private val overrideLookupLock = Any()

        private fun checkAllElementsMatch(array: Array<*>, array2: Array<*>): Boolean {
            if (array.size != array2.size)
                return false

            for (i in array.indices) {
                if (array[i] != array2[i])
                    return false
            }

            return true
        }

        /** [returnType] is null for [SporranHelper.hasMethodOverride]. */
        fun hasMethodOverride(topClass: Class<*>, superClass: Class<*>, methodName: String, methodArgs: Array<out Class<*>>, returnType: Class<*>?): Boolean {
            val classValue = findOverrideClassValue(superClass, methodName, methodArgs, returnType)
                ?: registerOverrideClassValue(OverrideData(superClass, methodName, methodArgs, returnType))

            return classValue.get(topClass)
        }

        // Sporran: look the ClassValue up before computeIfAbsent, whose lambda captured the arguments and so was allocated on
        //  every call, even though the key is almost always cached already.
        fun getOverrideClassValue(overrideData: OverrideData): ClassValue<Boolean> {
            return this.cachedClassValues[overrideData] ?: this.cachedClassValues.computeIfAbsent(overrideData, ::createOverrideClassValue)
        }

        private fun overrideLookupIndex(tableSize: Int, superClass: Class<*>, methodName: String, methodArgs: Array<out Class<*>>, returnType: Class<*>?): Int {
            var hash = superClass.hashCode()
            hash = 31 * hash + methodName.hashCode()

            for (methodArg in methodArgs) {
                hash = 31 * hash + methodArg.hashCode()
            }

            hash = 31 * hash + returnType.hashCode()

            return (hash xor (hash ushr 16)) and (tableSize - 1)
        }

        private fun findOverrideClassValue(superClass: Class<*>, methodName: String, methodArgs: Array<out Class<*>>, returnType: Class<*>?): ClassValue<Boolean>? {
            val table = this.overrideLookupTable
            var entry = table[overrideLookupIndex(table.size, superClass, methodName, methodArgs, returnType)]

            while (entry != null) {
                val overrideData = entry.overrideData

                // The same comparison as OverrideData#equals.
                if (overrideData.superClass == superClass && overrideData.returnType == returnType
                    && overrideData.methodName == methodName && checkAllElementsMatch(overrideData.methodArgs, methodArgs)
                ) {
                    return entry.classValue
                }

                entry = entry.next
            }

            return null
        }

        private fun registerOverrideClassValue(overrideData: OverrideData): ClassValue<Boolean> {
            val classValue = getOverrideClassValue(overrideData)

            synchronized(this.overrideLookupLock) {
                // Another thread may have added the same key in the meantime.
                if (findOverrideClassValue(overrideData.superClass, overrideData.methodName, overrideData.methodArgs, overrideData.returnType) == null) {
                    val oldTable = this.overrideLookupTable
                    val newTable: Array<OverrideLookupEntry?>

                    if ((this.overrideLookupSize + 1) * 2 > oldTable.size) {
                        newTable = arrayOfNulls(oldTable.size * 2)

                        for (oldEntry in oldTable) {
                            var entry = oldEntry

                            while (entry != null) {
                                addOverrideLookupEntry(newTable, entry.overrideData, entry.classValue)
                                entry = entry.next
                            }
                        }
                    } else {
                        newTable = oldTable.copyOf()
                    }

                    addOverrideLookupEntry(newTable, overrideData, classValue)
                    this.overrideLookupSize++
                    this.overrideLookupTable = newTable
                }
            }

            return classValue
        }

        private fun addOverrideLookupEntry(table: Array<OverrideLookupEntry?>, overrideData: OverrideData, classValue: ClassValue<Boolean>) {
            val index = overrideLookupIndex(table.size, overrideData.superClass, overrideData.methodName, overrideData.methodArgs, overrideData.returnType)
            table[index] = OverrideLookupEntry(overrideData, classValue, table[index])
        }

        private class OverrideLookupEntry(val overrideData: OverrideData, val classValue: ClassValue<Boolean>, val next: OverrideLookupEntry?)

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
    }

    /**
     * Sporran: a [hasMethodOverride] check for hot paths (several calls per entity per tick, e.g. pathfinding and fluid
     * lookups, or per block and per quad while chunks are meshed). Keep it in a static field; [test] then only costs a
     * [ClassValue] lookup, while [hasMethodOverride] allocates its varargs array and finds the check again on every call.
     * It shares the cache of [hasMethodOverride], so the result is always the same, and it resolves lazily: creating one
     * in a static initializer doesn't look the method up (that happens per class on the first [test]), and neither
     * creating nor asking one initialises [SporranHelper].
     */
    class MethodOverrideCheck private constructor(private val overrideData: OverrideData) {
        @Volatile
        private var classValue: ClassValue<Boolean>? = null

        fun test(topClass: Class<*>): Boolean {
            val classValue = this.classValue ?: MethodOverrideCache.getOverrideClassValue(overrideData).also { this.classValue = it }
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
