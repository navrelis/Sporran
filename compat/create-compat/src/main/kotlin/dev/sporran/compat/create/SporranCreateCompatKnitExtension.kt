package dev.sporran.compat.create

import dev.sporran.api.compatibility.SporranModCompatBridgeManager
import dev.sporran.api.compatibility.ModBridgeStrategy
import dev.sporran.compat.create.flywheel.FlywheelCompatBridge
import xyz.bluspring.knit.loader.api.KnitModScanSetupApi
import xyz.bluspring.knit.loader.api.KnitNativeModCompatExtension
import xyz.bluspring.knit.loader.mod.ModEnvironment

class SporranCreateCompatKnitExtension : KnitNativeModCompatExtension {
    override fun setupModScanning(api: KnitModScanSetupApi) {
        SporranModCompatBridgeManager.register("flywheel", environment = ModEnvironment.CLIENT, strategy = ModBridgeStrategy.PreferFabric("Detected Flywheel NeoForge, please install the latest Flywheel Fabric available from https://maven.createmod.net/dev/engine-room/flywheel or alternatively install the Vanillin mod to use Flywheel Fabric.")) {
            FlywheelCompatBridge.init()
        }

        SporranModCompatBridgeManager.register("colorwheel", enabledMixinConfigs = listOf("colorwheel.neoforge.mixins.json"), environment = ModEnvironment.CLIENT, strategy = ModBridgeStrategy.RequireBoth("Both the Fabric and the NeoForge versions of Colorwheel need to be installed to work correctly with Sporran!")) {
        }

        SporranModCompatBridgeManager.register("ponder", strategy = ModBridgeStrategy.PreferEither) { // To be honest, most Neo mods using Ponder are bundling it either way.
        }
    }
}
