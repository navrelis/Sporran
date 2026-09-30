package dev.sporran.loader.remap

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import it.unimi.dsi.fastutil.objects.Object2ReferenceMaps
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.stream.consumeAsFlow
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.MappingResolver
import net.fabricmc.loader.impl.game.GameProviderHelper
import net.fabricmc.loader.impl.launch.FabricLauncherBase
import net.fabricmc.loader.impl.launch.MappingConfiguration
import net.fabricmc.loader.impl.util.SystemProperties
import net.fabricmc.mapping.tree.TinyMappingFactory
import net.minecraftforge.fart.api.ClassProvider
import net.minecraftforge.fart.internal.EnhancedClassRemapper
import net.minecraftforge.fart.internal.EnhancedRemapper
import net.minecraftforge.fart.internal.RenamingTransformer
import net.minecraftforge.srgutils.IMappingFile
import net.minecraftforge.srgutils.INamedMappingFile
import org.apache.commons.codec.digest.DigestUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.slf4j.LoggerFactory
import dev.sporran.Sporran
import dev.sporran.loader.SporranFlags
import dev.sporran.loader.SporranLoader
import dev.sporran.loader.remap.fixers.*
import dev.sporran.loader.remap.fixers.mixin.*
import dev.sporran.loader.remap.resource.IgnoreSignatureResourceRemapper
import dev.sporran.loader.remap.resource.ManifestResourceRemapper
import dev.sporran.util.CaseInsensitiveStringHashSet
import dev.sporran.util.ClassNameHashSet
import dev.sporran.util.SporranHelper
import dev.sporran.util.SporranTimings
import dev.sporran.workarounds.ModifiedCloneWorkaroundLoader
import xyz.bluspring.knit.loader.mod.ModDefinition
import xyz.bluspring.knit.loader.util.collect
import xyz.bluspring.knit.loader.util.concurrent
import xyz.bluspring.knit.loader.util.launchIn
import xyz.bluspring.knit.loader.util.onEach
import java.io.File
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.*
import java.util.function.Consumer
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.jar.Manifest
import kotlin.io.path.*
import kotlin.time.measureTime

object SporranRemapper {
    // Keeps track of the remapper changes, so every time I update the remapper,
    // it remaps all the mods following the remapper changes.
    // this can update by like 12 versions in 1 update, so don't worry too much about it.
    const val REMAPPER_VERSION = 255
    const val MC_MAPPED_JAR_VERSION = 10

    // Sporran JVM flags
    private val forceRemap = SporranFlags.FORCE_REMAPPING
    private val disableRemaps = SporranFlags.DISABLE_REMAPPING
    internal val forceProductionRemap = SporranFlags.FORCE_PRODUCTION_REMAPPING

    // GSON stuff for pretty printing
    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .create()

    val logConsumer = Consumer<String> {
        logger.debug(it)
    }

    internal val logger = LoggerFactory.getLogger("Sporran Remapper")
    // internal val useNamed = FabricLoader.getInstance().mappingResolver.currentRuntimeNamespace != "intermediary"

    // Remapper extensions
    fun MappingResolver.mapClass(clazz: Class<*>): String = mapClassName("intermediary", "net.minecraft.$clazz").replace(".", "/")

    private val mappingResolver = if (forceProductionRemap)
        NoopMappingResolver()
    else
        FabricLoader.getInstance().mappingResolver

    // This is created automatically using https://github.com/BluSpring/srg2intermediary
    // moj -> intermediary
    val mojIntermediaryMapping: IMappingFile = SporranTimings.time("loading moj_intermediary.tiny") { this::class.java.getResourceAsStream("/moj_intermediary.tiny")!!.buffered().use { IMappingFile.load(it) } }
    // Sporran: only the development environment needs Fabric's own mappings, so don't parse them on every production start.
    val fabricMappings: INamedMappingFile by lazy { MappingConfiguration::class.java.classLoader.getResourceAsStream("mappings/mappings.tiny")!!.use { INamedMappingFile.load(it) } }

    private val devMojIntermediaryMapping: IMappingFile = SporranTimings.time("renaming moj_intermediary to runtime names") { mojIntermediaryMapping.run {
        if (!forceProductionRemap)
            this.rename(DevMappingRenamer(FabricLoader.getInstance().mappingResolver))
        else
            this
    } }

    val intermediaryMojMapping: IMappingFile = SporranTimings.time("reversing moj_intermediary") { mojIntermediaryMapping.reverse() }
    private val devIntermediaryMojMapping = SporranTimings.time("reversing runtime moj_intermediary") { devMojIntermediaryMapping.reverse() }

    // if for whatever reason we need these, they're available
    //val mojIntermediaryTree = TinyConverter.convert(mojIntermediaryMapping, "moj", "intermediary")
    //val intermediaryMojTree = TinyConverter.convert(intermediaryMojMapping, "intermediary", "moj")

    // Some workaround mappings to remap some names to Sporran equivalents.
    // This fixes some compatibility issues.
    private val sporranWorkaroundTree = TinyMappingFactory.load(
        this::class.java.getResourceAsStream("/sporran_workaround_mappings.tiny")!!.bufferedReader()
    )

    lateinit var enhancedRemapper: SporranEnhancedRemapper
    lateinit var enhancedInverseRemapper: SporranEnhancedRemapper

    private lateinit var remappedModsDir: Path

    // Moj name -> (parent class name, intermediary/mapped name)
    val mojMappedFields = Object2ReferenceMaps.synchronize(Object2ReferenceOpenHashMap<String, MutableMap<String, String>>())

    // Moj name -> (parent class name, (intermediary/mapped name, descriptor))
    val mojMappedMethods = Object2ReferenceMaps.synchronize(Object2ReferenceOpenHashMap<String, MutableMap<String, MutableSet<Pair<String, String>>>>())

    init {
        // Magical field references that are required because otherwise the remapper will deadlock
        val mojIntermediaryMapping = devMojIntermediaryMapping
        val forceProductionRemap = forceProductionRemap
        val mappingResolver = mappingResolver
        val mojMappedFields = mojMappedFields
        val mojMappedMethods = mojMappedMethods

        val walkStart = System.nanoTime()
        runBlocking {
            launch(Dispatchers.IO) {
                mojIntermediaryMapping.classes.asFlow().concurrent().collect {
                    it.fields.asFlow().concurrent().collect { f ->
                        val map = mojMappedFields.getOrPut(f.original) {
                            Object2ReferenceMaps.synchronize(Object2ReferenceOpenHashMap())
                        }
                        val mapped = if (!forceProductionRemap)
                            mappingResolver.mapFieldName(
                                "intermediary",
                                it.mapped.replace("/", "."),
                                f.mapped,
                                f.mappedDescriptor
                            )
                        else
                            f.mapped

                        map[f.parent.original] = mapped
                    }

                    it.methods.asFlow().concurrent().collect { f ->
                        val map = mojMappedMethods.getOrPut(f.original) {
                            Object2ReferenceMaps.synchronize(Object2ReferenceOpenHashMap())
                        }
                        val mapped = if (!forceProductionRemap)
                            mappingResolver.mapMethodName(
                                "intermediary",
                                it.mapped.replace("/", "."),
                                f.mapped,
                                f.mappedDescriptor
                            )
                        else
                            f.mapped

                        map.computeIfAbsent(f.parent.original) { mutableSetOf() }
                            .add(mapped to f.mappedDescriptor)
                    }
                }
            }.join()
        }
        SporranTimings.log("moj -> runtime member table", System.nanoTime() - walkStart)
    }

    fun init() {}

    private fun initEnhancedRemapper(intermediaryMap: Path?, modLoadingQueue: Collection<ModDefinition>): ClassProvider {
        // Initialize this a bit later, cuz we need the same libraries.
        return ClassProvider.builder().apply {
            // time to add Intermediary mappings to the mix! :,D
            if (FabricLoader.getInstance().isDevelopmentEnvironment && !forceProductionRemap) {
                addLibrary(intermediaryMap)
            }

            // IMPORTANT: this cannot be a flow or use merge, otherwise the order isn't retained. mojGamePath MUST be at the top of the list.
            listOf(
                // List down NeoForge paths
                *SporranHelper.getSporranPaths().toTypedArray(),
                // Add all Fabric mods
                *FabricLoader.getInstance().allMods
                    .flatMap { container -> container.rootPaths }.toTypedArray(),
                // add mapped path too
                *runBlocking { getGameClassPath() },
                // Add all NeoForge mods to the library path, because dependencies don't have to be specified
                // in order to use mods lmao
                *modLoadingQueue.map { mod -> mod.path }.toTypedArray()
            ).forEach {
                addLibrary(it)
            }
        }.build()
    }

    /**
     * @param modHashes the MD5 of every mod jar, see [hashModJars]. Jars missing from it are hashed here.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun remapMods(modLoadingQueue: Collection<ModDefinition>, remappedModsDir: Path, modHashes: Map<ModDefinition, String> = emptyMap()) {
        if (disableRemaps) {
            logger.warn("Mod remapping has been disabled! Mods built normally using ForgeGradle will not function with this enabled.")
            logger.warn("Only have this enabled if you know what you're doing!")

            return
        }

        this.remappedModsDir = remappedModsDir

        if (forceRemap)
            logger.warn("Forced remaps enabled! All NeoForge mods will be remapped.")

        // Forcefully initialize the mapping resolver.
        // Otherwise, it just crashes from trying to initialize concurrently.
        mappingResolver.unmapClassName("intermediary", "net.minecraft.class_1937")

        // Sporran: every mod jar is hashed only once, see hashModJars.
        val missingHashes = modLoadingQueue.filter { !it.isBuiltin && !modHashes.containsKey(it) }
        val modHashes = if (missingHashes.isEmpty()) modHashes else IdentityHashMap(modHashes).apply { putAll(hashModJars(missingHashes)) }

        // Automatically delete outdated remapped versions
        SporranTimings.time("stale remap cache sweep") {
            val markedForDeletion = mutableListOf<Path>()

            SporranLoader.sporranCacheDir.walk().forEach {
                if (it.extension != "jar")
                    return@forEach

                if (it.nameWithoutExtension.startsWith("minecraft_") &&
                    (
                        !it.nameWithoutExtension.contains(SporranLoader.MC_VERSION.friendlyString) ||
                        !it.nameWithoutExtension.endsWith("_$MC_MAPPED_JAR_VERSION")
                    )
                ) {
                    markedForDeletion.add(it)
                }
            }

            remappedModsDir.walk().forEach { file ->
                if (file.extension != "jar" && file.extension != "tmp")
                    return@forEach

                val mod = modLoadingQueue.firstOrNull { file.nameWithoutExtension.startsWith(it.id) }

                if (mod == null) {
                    markedForDeletion.add(file)
                    return@forEach
                }

                val fileNameSplit = file.nameWithoutExtension.removePrefix("${mod.id}_").split("_")
                val fileRemapperVersion = fileNameSplit[0].toIntOrNull()
                val fileHash = fileNameSplit[1]

                if (fileRemapperVersion == null) {
                    markedForDeletion.add(file)
                    return@forEach
                }

                if (fileRemapperVersion != REMAPPER_VERSION) {
                    markedForDeletion.add(file)
                    return@forEach
                }

                if (mod.isBuiltin) {
                    markedForDeletion.add(file)
                    return@forEach
                }

                val currentHash = modHashes[mod]

                if (currentHash != fileHash) {
                    markedForDeletion.add(file)
                    return@forEach
                }
            }

            for (path in markedForDeletion) {
                path.deleteIfExists()
            }
        }

        mojGamePath = SporranTimings.time("remapMinecraft") { remapMinecraft("mojang", devIntermediaryMojMapping) }

        val exception = RuntimeException("Failed to remap NeoForge mods in Sporran!")

        logger.info("Remapping NeoForge mods...")

        val mods = modLoadingQueue.filter { !it.isBuiltin }.toSet()

        // Use the regular mod file
        val classProvider = SporranTimings.time("building the remapper class provider") { ClassProvider.builder().apply {
            // IMPORTANT: this cannot be a flow or use merge, otherwise the order isn't retained. mojGamePath MUST be at the top of the list.
            listOf(
                mojGamePath,
                // List down NeoForge paths
                *SporranHelper.getSporranPaths().toTypedArray(),
                // Add all Fabric mods
                *FabricLoader.getInstance().allMods
                    .flatMap { container -> container.rootPaths }.toTypedArray(),
                // add mapped path too
                *getGameClassPath(),
                // Add all NeoForge mods to the library path, because dependencies don't have to be specified
                // in order to use mods lmao
                *modLoadingQueue.map { mod -> mod.path }.toTypedArray()
            ).forEach {
                addLibrary(it)
            }
        }.build() }

        val intermediaryMap = if (FabricLoader.getInstance().isDevelopmentEnvironment && !forceProductionRemap)
            remapMinecraft("intermediary", fabricMappings.getMap("named", "intermediary").rename(DevMojClassMappingRenamer(FabricLoader.getInstance().mappingResolver)))
        else null

        // Initialize a global remapper state
        enhancedRemapper = SporranEnhancedRemapper(classProvider, devMojIntermediaryMapping, logConsumer) {
            initEnhancedRemapper(intermediaryMap, modLoadingQueue)
        }
        enhancedInverseRemapper = SporranEnhancedRemapper(classProvider, devIntermediaryMojMapping, logConsumer) {
            initEnhancedRemapper(intermediaryMap, modLoadingQueue)
        }

        //val mixinRemapper = SporranMixinRemapper(enhancedRemapper, mojIntermediaryMapping, classProvider)
        val resourceRemappers = listOf(
            ManifestResourceRemapper,
            IgnoreSignatureResourceRemapper
        )

        if (FabricLoader.getInstance().isDevelopmentEnvironment) {
            enhancedRemapper.initDevRemapper()
            enhancedInverseRemapper.initDevRemapper()
        }

        suspend fun remapMod(file: Path, mod: ModDefinition) {
            val exception = RuntimeException("Failed to remap NeoForge mod ${mod.displayName} (${mod.id})!")
            var allowSoftFail = false

            if (mod.isBuiltin) { // Prevent Sporran from remapping *directly* NeoForge mods. Yes, that started happening.
                return
            }

            // If JiJ'd libraries fail to remap, we can mark it as a "soft failure".
            if (mod.additionalData["isJiJ"] == true && (mod.additionalData["manifest"] as? Manifest?)?.mainAttributes?.getValue("FMLModType") != "GAMELIBRARY") {
                allowSoftFail = true
            }

            val hash = modHashes[mod] ?: withContext(Dispatchers.IO) { file.inputStream().use { DigestUtils.md5Hex(it) } }
            val modifiedJarFile = SporranRemapper.remappedModsDir / "${mod.id}_${REMAPPER_VERSION}_$hash.jar"

            if (modifiedJarFile.exists() && !forceRemap) {
                mod.path = modifiedJarFile
                return
            }

            // Make a temporary file, so if any errors occur in remapping, we don't end up with a broken state.
            val tempModifiedJarFile = SporranRemapper.remappedModsDir / "${mod.id}_${REMAPPER_VERSION}_$hash.jar.tmp"
            val jar = withContext(Dispatchers.IO) { JarFile(file.toFile()) }
            val jarOutputStream = withContext(Dispatchers.IO) { JarOutputStream(tempModifiedJarFile.outputStream(StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) }

            val entryToClassNodes = Object2ReferenceMaps.synchronize(Object2ReferenceOpenHashMap<JarEntry, ClassNode>())
            // val classesToProcess = entryToClassNodes.values.intersect(SporranHelper.getForgeClassNodes().toSet()).toList()

            val mixinClasses = Collections.synchronizedSet(ClassNameHashSet())
            val refmaps = Collections.synchronizedSet(CaseInsensitiveStringHashSet())
            val mixinRefmaps = Collections.synchronizedMap<JarEntry, MixinRefmap>(mutableMapOf())

            // JAR validation information stripping.
            // If we can find out how to use this to our advantage prior to remapping,
            // we may still be able to use this information safely.
            val manifestEntry = jar.getJarEntry("META-INF/MANIFEST.MF")
            if (manifestEntry != null) {
                val manifest = jar.getInputStream(manifestEntry).use { Manifest(it) }
                val mixinConfigs = manifest.mainAttributes.getValue("MixinConfigs")?.split(",")?.toMutableSet() ?: mutableSetOf()

                // Search for more mixin configs, because apparently it's possible to define with a fucking class.
                for (entry in jar.stream()) {
                    if (entry.name.endsWith(".mixin.json") || entry.name.endsWith(".mixins.json")) {
                        mixinConfigs.add(entry.name)
                    } else if (entry.name.endsWith(".refmap.json")) {
                        // Search for more refmaps too, because they're probably defined in class too.
                        refmaps.add(entry.name)
                    }
                }

                // Read mixin configs and add them to the list of mixins to fix
                mixinConfigs.asFlow().collect { config ->
                    val jsonEntry = jar.getJarEntry(config) ?: return@collect
                    val json = jar.getInputStream(jsonEntry).use {
                        JsonParser.parseReader(it.reader())
                    }.asJsonObject

                    if (!json.has("package")) return@collect

                    val mixinPackage = json.get("package").asString

                    merge(
                        (json.get("mixins") as? JsonArray)?.asFlow() ?: emptyFlow(),
                        (json.get("client") as? JsonArray)?.asFlow() ?: emptyFlow(),
                        (json.get("server") as? JsonArray)?.asFlow() ?: emptyFlow()
                    ).collect {
                        if (!it.isJsonNull)
                            mixinClasses.add("$mixinPackage.${it.asString}")
                    }

                    runCatching { json.get("refmap")!!.asString }.onSuccess { refmaps.add(it) }
                }
            }

            withContext(Dispatchers.IO) {
                val seen = HashSet<String>()
                jar.stream().consumeAsFlow().collect { entry ->
                    // Ars Nouveau's jar has duplicate entries. wtf?????
                    if (!seen.add(entry.name)) {
                        logger.warn("Mod ${mod.displayName} (${mod.id})'s jar file is malformed; saw duplicate of ${entry.name}!")
                        return@collect
                    }
                    // Transform some specific files
                    for (remapper in resourceRemappers) {
                        if (remapper.canTransform(entry.name)) {
                            val data = jar.getInputStream(entry).use { remapper.transform(entry.name, it) }

                            if (data != null) {
                                jarOutputStream.putNextEntry(entry)
                                jarOutputStream.write(data)
                                jarOutputStream.closeEntry()
                            }

                            return@collect
                        }
                    }

                    // Store refmap JSONs
                    if (refmaps.contains(entry.name)) {
                        val json = jar.getInputStream(entry).use { it.reader(Charsets.UTF_8).use { r -> JsonParser.parseReader(r) } }

                        if (json.isJsonObject) {
                            val obj = json.asJsonObject

                            // Be careful, because they might not even have the data in the first place.
                            if (obj.has("mappings")) {
                                mixinRefmaps[entry] = MixinRefmap(
                                    Collections.synchronizedMap(
                                        obj.getAsJsonObject("mappings").asMap()
                                            .map {
                                                it.key to it.value.asJsonObject.asMap().map { b ->
                                                    b.key to b.value.asString
                                                }
                                                    .associate { b -> b.first to b.second }
                                                    .toMutableMap()
                                            }
                                            .associate { it.first to it.second }
                                            .toMutableMap()
                                    ),
                                    Collections.synchronizedMap(mutableMapOf())
                                )
                            }
                        }

                        return@collect
                    }

                    if (entry.name.endsWith(".class")) {
                        // Add the class files for remapping
                        val classReader = jar.getInputStream(entry).use { ClassReader(it) }

                        // we need the info for this for the class writer
                        val classNode = ClassNode(Opcodes.ASM9)
                        classReader.accept(classNode, 0)

                        entryToClassNodes[entry] = classNode
                    } else {
                        // Otherwise, add the entry directly if everything's already been processed.
                        jarOutputStream.putNextEntry(entry)
                        jar.getInputStream(entry).use { it.copyTo(jarOutputStream) }
                        jarOutputStream.closeEntry()
                    }
                }
            }

            fun isMixinClass(node: ClassNode): Boolean {
                return node.name in mixinClasses ||
                    // GUESS WHAT, SOME MODS DON'T FUCKING DEFINE SOME MIXINS IN THE FILE, INSTEAD IN THE MIXIN PLUGIN.
                    // SO LET'S JUST RUN THIS ON EVERYTHING THAT HAS THE BLOODY ANNOTATION.
                    SporranHelper.mergeNullableCollections(node.visibleAnnotations, node.invisibleAnnotations)
                        .any { it.desc == MixinTypes.MIXIN.descriptor }
            }

            // Make copies of the original ClassNode objects so we can use them as reference to remap inherited shadows.
            val originalMappings = mutableMapOf<String, ClassNode>()
            for (node in entryToClassNodes.values) {
                if (isMixinClass(node)) {
                    val writer = ClassWriter(0)
                    node.accept(writer)
                    val nodeCopy = ClassReader(writer.toByteArray())
                    val newNode = ClassNode()
                    nodeCopy.accept(newNode, 0)
                    originalMappings[node.name] = newNode
                }
            }

            val throwable = entryToClassNodes.entries.asFlow().concurrent().runCatching {
                this.collect { (entry, originalNode) ->
                    try {
                        val remappedNode = ClassNode(Opcodes.ASM9)

                        // only do this on mixin classes, please
                        // We must remap the mixins before actually remapping them to Intermediary, so the names are correct in prod.
                        if (isMixinClass(originalNode)) {
                            MixinRemapper.remapClass(originalNode, enhancedRemapper, mixinRefmaps.values)
                            MixinShadowRemapper.remapClass(originalNode, enhancedRemapper, originalMappings)

                            if (!SporranFlags.DISABLE_FIXERS) {
                                MixinAdditionalRemapper.remapClass(originalNode)
                                MixinStaticMethodFixer.fixClass(originalNode)
                                MixinDirectModifierFixer.fixClass(originalNode)
                            }
                        }

                        originalNode.accept(EnhancedClassRemapper(remappedNode, enhancedRemapper, RenamingTransformer(enhancedRemapper, false)))

                        if (!SporranFlags.DISABLE_FIXERS) {
                            ConditionalInterfaceInjectionFixer.fixClass(remappedNode)
                            EventClassVisibilityFixer.fixClass(remappedNode)
                            InjectedInterfaceVisibilityFixer.fixClass(remappedNode)
                            ObjectHolderDefinalizer.processClass(remappedNode)
                            WorkaroundFixer.fixClass(remappedNode)
                            ConflictingStaticMethodFixer.fixClass(remappedNode)
                            EnvironmentRemapper.remapClass(remappedNode)
                            EnvironmentLambdaFixer.fixClass(remappedNode)
                            RemoveModulesFixer.fixClass(remappedNode)
                            ModifiedCloneWorkaroundLoader.fixClass(remappedNode)
                            GLVersionSpecifierFixer.fixClass(remappedNode)
                        }

                        val writer = ClassWriter(Opcodes.ASM9)
                        remappedNode.accept(writer)

                        synchronized(jarOutputStream) {
                            jarOutputStream.putNextEntry(entry)
                            jarOutputStream.write(writer.toByteArray())
                            jarOutputStream.closeEntry()
                        }
                    } catch (e: Throwable) {
                        throw RuntimeException("Failed to remap ${entry.name}", e)
                    }
                }
            }.exceptionOrNull()

            if (throwable != null) {
                exception.addSuppressed(throwable)
            }
            
            // If for whatever reason the refmap remapping missed something, we need to remap it immediately.
            MixinRemapper.remapUnmappedRefmaps(mixinRefmaps.values, enhancedRemapper)

            // Now, let's write the refmap JSONs
            for ((entry, refmap) in mixinRefmaps) {
                val json = JsonObject()

                // First, write the mappings directly.
                val mappings = JsonObject()
                for ((mixinClass, nameMappings) in refmap.mappings) {
                    val classMapping = JsonObject()
                    for ((named, obfuscated) in nameMappings) {
                        classMapping.addProperty(named, obfuscated)
                    }

                    mappings.add(mixinClass, classMapping)
                }
                json.add("mappings", mappings)

                // Now, let's add the mapping data map directly to "named:intermediary"
                val dataMap = JsonObject()
                dataMap.add("named:intermediary", mappings)

                json.add("data", dataMap)

                jarOutputStream.putNextEntry(entry)
                jarOutputStream.write(gson.toJson(json).toByteArray(Charsets.UTF_8))
                jarOutputStream.closeEntry()
            }

            jarOutputStream.close()
            jar.close()

            if (exception.suppressed.isNotEmpty()) {
                // Soft fail if a JiJ'd mod failed to remap.
                if (allowSoftFail) {
                    logger.warn("Failed to remap ${mod.id}, but it's a library mod so we should be able to safely ignore it.", exception)
                    return
                }

                throw exception
            } else {
                // We've finished writing the mod file successfully, so let's not make it temporary anymore.
                tempModifiedJarFile.moveTo(modifiedJarFile, true)
            }

            mod.path = modifiedJarFile

            return
        }

        coroutineScope {
            mods.asFlow().concurrent()
                .onEach { mod ->
                    runCatching {
                        logger.info("Remapping ${mod.displayName} (${mod.id})")
                        val ms = measureTime {
                            remapMod(mod.path, mod)
                        }.inWholeMilliseconds
                        logger.info("Remapped ${mod.displayName} (${mod.id}) [took ${ms}ms]")
                    }.onFailure {
                        logger.error("Failed to remap ${mod.displayName} (${mod.id})", it)
                        if (it is Exception) {
                            exception.addSuppressed(it)
                        }
                    }
                }.launchIn(this).join()
        }

        // Clear any caches stored in the fixers
        MixinRemapper.clearCache()
        enhancedRemapper.clearCaches()
        enhancedInverseRemapper.clearCaches()

        logger.info("Finished remapping mods!")

        if (exception.suppressed.isNotEmpty()) {
            logger.error("Ran into some errors during the remapping process, cannot continue!")
            throw exception
        }
    }

    @JvmOverloads
    fun remapClass(name: String, toIntermediary: Boolean = false, ignoreWorkaround: Boolean = false): String {
        val workaround = if (!ignoreWorkaround)
            sporranWorkaroundTree.classes.firstOrNull { it.getRawName("forge") == name }?.getRawName("sporran")
        else null
        val intermediary = mojIntermediaryMapping.remapClass(name.replace(".", "/"))
        if (toIntermediary) {
            return workaround ?: intermediary ?: name
        }

        return (workaround ?: if (intermediary != null)
            mappingResolver.mapClassName("intermediary", intermediary.replace("/", ".")) ?: name
        else name).replace(".", "/")
    }

    /**
     * Converts Intermediary mappings to Moj mappings.
     */
    fun unmapClass(name: String): String {
        val intermediary = mappingResolver.unmapClassName("intermediary", name.replace("/", "."))
        return intermediaryMojMapping.remapClass(intermediary.replace(".", "/"))
    }

    val gameFile = getMCGameFile()
    lateinit var mojGamePath: Path

    private fun getDeobfJarDir(gameDir: Path, gameId: String, gameVersion: String): Path {
        return GameProviderHelper::class.java
            .getDeclaredMethod("getDeobfJarDir", Path::class.java, String::class.java, String::class.java)
            .apply {
                isAccessible = true
            }
            .invoke(null, gameDir, gameId, gameVersion) as Path
    }

    private fun getMCGameFile(): Path? {
        if (!FabricLoader.getInstance().isDevelopmentEnvironment) {
            // broken on servers.
            /*val commonJar = GameProviderHelper.getCommonGameJar()

            if (commonJar != null)
                return commonJar

            val sidedJar = GameProviderHelper.getEnvGameJar(FabricLoader.getInstance().environmentType)

            if (sidedJar != null)
                return sidedJar*/

            // this gives the obfuscated JAR; we don't want that
            //val inputGameJar = FabricLoader.getInstance().objectShare.get("fabric-loader:inputGameJar")
            //if (inputGameJar is Path)
            //return inputGameJar.toFile()

            // This is our best bet towards getting the Intermediary JAR.
            val deobfJar =
                getDeobfJarDir(
                    FabricLoader.getInstance().gameDir,
                    "minecraft",
                    SporranLoader.MC_VERSION.friendlyString
                ) / "${FabricLoader.getInstance().environmentType.name.lowercase()}-${FabricLoader.getInstance().mappingResolver.currentRuntimeNamespace}.jar"

            if (deobfJar.exists())
                return deobfJar
        } else {
            // TODO: is there a better way of doing this?
            val possibleMcGameJar = FabricLauncherBase.getLauncher().classPath.firstOrNull { path ->
                val str = path.absolutePathString()
                str.contains("net") && str.contains("minecraft") && str.contains("-loom.mappings.") && str.contains("minecraft-merged-")
            }

            return possibleMcGameJar
        }

        return null
    }

    suspend fun getGameClassPath(): Array<out Path> {
        return if (!FabricLoader.getInstance().isDevelopmentEnvironment)
            arrayOf(
                getMCGameFile()
                    ?: FabricLoader.getInstance().objectShare.get("fabric-loader:inputGameJar") as Path,
                Sporran::class.java.protectionDomain.codeSource.location.toURI().toPath()
            )
        else
            mutableListOf<Path>().apply {
                val remapClasspathFile = System.getProperty(SystemProperties.REMAP_CLASSPATH_FILE)
                    ?: throw RuntimeException("No remapClasspathFile provided")

                val content = withContext(Dispatchers.IO) { Path(remapClasspathFile).readText() }

                this.addAll(
                    content.split(File.pathSeparator.toRegex())
                        .dropLastWhile { it.isEmpty() }
                        .map { Path(it) }
                )

                this.add(Sporran::class.java.protectionDomain.codeSource.location.toURI().toPath())
            }.toTypedArray()
    }

    private suspend fun remapMinecraft(mappingName: String, mappingFile: IMappingFile): Path {
        val mojFile =
            SporranLoader.sporranCacheDir / "minecraft_${SporranLoader.MC_VERSION.friendlyString}-${mappingName}_$MC_MAPPED_JAR_VERSION.jar"

        if (mojFile.exists() && !forceRemap) {
            logger.info("${mappingName}-mapped Minecraft JAR detected, not creating a new remapped file.")
            return mojFile
        }

        if (gameFile == null) {
            throw IllegalStateException("Minecraft JAR was not found!")
        }

        val tempMojFile =
            SporranLoader.sporranCacheDir / "minecraft_${SporranLoader.MC_VERSION.friendlyString}-${mappingName}_$MC_MAPPED_JAR_VERSION.jar.tmp"

        if (tempMojFile.exists())
            tempMojFile.deleteIfExists()

        logger.info("Creating ${mappingName}-mapped Minecraft JAR for remapping NeoForge mods...")
        val startTime = System.currentTimeMillis()

        val classProvider = ClassProvider.builder().apply {
            this.addLibrary(gameFile)
            for (path in getGameClassPath()) {
                this.addLibrary(path)
            }
        }.build()
        val mojRemapper = EnhancedRemapper(classProvider, mappingFile, logConsumer)

        val gameJar = withContext(Dispatchers.IO) { JarFile(gameFile.toFile()) }
        runCatching { tempMojFile.createFile() }

        tempMojFile.outputStream().use { outputStream ->
            withContext(Dispatchers.IO) { JarOutputStream(outputStream) }.use { outputJar ->
                // Sporran: remap the classes concurrently, like remapMod does (this was ~7 s on one thread). The entry order of
                //  this jar doesn't matter, it is only ever read as a class library through FART's ClassProvider.
                gameJar.stream().consumeAsFlow().flowOn(Dispatchers.IO).concurrent()
                    .collect { entry ->
                        if (entry.name.endsWith(".class")) {
                            val classReader = gameJar.getInputStream(entry).use { ClassReader(it) }

                            val classNode = ClassNode(Opcodes.ASM9)
                            classReader.accept(classNode, 0)

                            val classWriter = ClassWriter(0)

                            val visitor = EnhancedClassRemapper(classWriter, mojRemapper, RenamingTransformer(mojRemapper, false))
                            classNode.accept(visitor)
                            ConflictingStaticMethodFixer.fixClass(classNode)

                            // We need to remap to the correct name, otherwise the remapper completely fails in production environments.
                            val mojName = mappingFile.remapClass(entry.name.removePrefix("/").removeSuffix(".class"))
                            val bytes = classWriter.toByteArray()

                            synchronized(outputJar) {
                                outputJar.putNextEntry(JarEntry("$mojName.class"))
                                outputJar.write(bytes)
                                outputJar.closeEntry()
                            }
                        } else {
                            val bytes = gameJar.getInputStream(entry).use { it.readAllBytes() }

                            synchronized(outputJar) {
                                outputJar.putNextEntry(entry)
                                outputJar.write(bytes)
                                outputJar.closeEntry()
                            }
                        }
                    }
            }
        }

        tempMojFile.moveTo(mojFile, true)

        logger.info("Remapped Minecraft to $mappingName. (took ${System.currentTimeMillis() - startTime} ms)")

        return mojFile
    }

    @JvmOverloads
    fun remapDescriptor(descriptor: String, reverse: Boolean = false, toIntermediary: Boolean = false): String {
        var formedString = ""

        var incompleteString = ""
        var isInClass = false
        descriptor.forEach {
            if (it == 'L' && !isInClass)
                isInClass = true

            if (isInClass) {
                incompleteString += it

                if (it == ';') {
                    isInClass = false

                    formedString += 'L'

                    val name = incompleteString.removePrefix("L").removeSuffix(";")
                    formedString += if (!reverse)
                        remapClass(name, toIntermediary)
                    else
                        unmapClass(name)

                    formedString += ';'

                    incompleteString = ""
                }
            } else {
                formedString += it
            }
        }

        return formedString
    }
}

/**
 * Sporran: the MD5 of every non-built-in mod jar, hashed once and concurrently. The stale cache sweep and remapMod in
 * [SporranRemapper.remapMods] used to hash each jar on their own (the sweep one jar after another), so Cataclysm's 73 MB jar
 * was read twice on every start. This is not part of [SporranRemapper], so that it can run while [SporranRemapper] is still
 * being initialized (parsing its mappings).
 */
suspend fun hashModJars(definitions: Collection<ModDefinition>): Map<ModDefinition, String> {
    val hashes = coroutineScope {
        definitions.filter { !it.isBuiltin }
            .map { mod -> async(Dispatchers.IO) { mod to mod.path.inputStream().use { DigestUtils.md5Hex(it) } } }
            .awaitAll()
    }

    // Keyed by identity: ModDefinition is a data class, and remapMod changes its (hashed) path.
    return IdentityHashMap<ModDefinition, String>().apply { hashes.forEach { (mod, hash) -> put(mod, hash) } }
}
