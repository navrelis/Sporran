package dev.sporran.compat.fabric

import net.fabricmc.loader.api.FabricLoader
import dev.sporran.api.compatibility.SporranModCompatBridgeManager
import dev.sporran.api.compatibility.ModBridgeStrategy
import dev.sporran.compat.fabric.architectury.SporranArchitecturyApiCompat
import dev.sporran.compat.fabric.automodpack.SporranAutoModpackCompat
import dev.sporran.compat.fabric.everycompat.EveryCompatBridge
import dev.sporran.compat.fabric.geckolib.GeckoLibEvents
import dev.sporran.compat.fabric.resourcefullib.ResourcefulLibBridgeStrategy
import dev.sporran.compat.fabric.sable.SableCompatBridge
import dev.sporran.compat.fabric.veil.VeilCompatBridge
import xyz.bluspring.knit.loader.api.KnitModScanSetupApi
import xyz.bluspring.knit.loader.api.KnitNativeModCompatExtension
import xyz.bluspring.knit.loader.mod.ModEnvironment

class SporranFabricCompatsKnitExtension : KnitNativeModCompatExtension {
    override fun setupModScanning(api: KnitModScanSetupApi) {
        if (FabricLoader.getInstance().isModLoaded("automodpack")) {
            SporranAutoModpackCompat.modpackDir?.let { path ->
                for (modDir in api.loader.modDirs) {
                    if (modDir.isAbsolute) continue
                    api.addModDirectory(path.resolve(modDir))
                }
            }
        }

        // Funny little workaround to avoid classload issues in mixin later
        if (FabricLoader.getInstance().isModLoaded("sable")) {
            try {
                Class.forName($$"dev.ryanhcode.sable.mixin.AbstractSableMixinPlugin$MixinConstraints")
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        /*
        listOf(
            "FreeNativeResources",
            "VeilAddShaderProcessors",
            "VeilDynamicBuffersChanged",
            "VeilPostProcessing",
            "VeilRegisterBlockLayers",
            "VeilRegisterFixedBuffers",
            "VeilRegisterGlobalControllers",
            "VeilRendererAvailable",
            "VeilShaderCompile"
        ).map { "foundry.veil.forge.event.Forge${it}Event" }
         */
        SporranModCompatBridgeManager.register("sable", listOf("sable-neoforge.mixins.json"), strategy = ModBridgeStrategy.RequireBoth) {
            SableCompatBridge.init()
        }

        SporranModCompatBridgeManager.register("veil", environment = ModEnvironment.CLIENT, strategy = ModBridgeStrategy.PreferFabric) {
            VeilCompatBridge.init()
        }

        SporranModCompatBridgeManager.register("geckolib", strategy = ModBridgeStrategy.PreferFabric, environment = ModEnvironment.CLIENT) {
            GeckoLibEvents.init()
        }

        SporranModCompatBridgeManager.register("architectury", strategy = ModBridgeStrategy.PreferFabric) {
            SporranArchitecturyApiCompat.initCommon()
        }

        SporranModCompatBridgeManager.register("computercraft", strategy = ModBridgeStrategy.PreferFabric) {}

        SporranModCompatBridgeManager.register("resourcefullib", strategy = ResourcefulLibBridgeStrategy) {}

        SporranModCompatBridgeManager.register("everycomp", strategy = ModBridgeStrategy.PreferFabric) {
            EveryCompatBridge.init()
        }

        SporranModCompatBridgeManager.register("jade", strategy = ModBridgeStrategy.PreferFabric) {}

        SporranModCompatBridgeManager.register("emi", strategy = ModBridgeStrategy.PreferFabric) {}
    }
}
