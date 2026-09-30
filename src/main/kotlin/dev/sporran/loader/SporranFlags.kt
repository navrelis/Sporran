package dev.sporran.loader

object SporranFlags {
    // Mainly for debugging, so already-remapped Forge mods will be remapped again.
    @JvmField val FORCE_REMAPPING = "sporran.forceRemap".checkPropertyBoolean()

    // Mainly for debugging, used to test unobfuscated mods and ensure that Sporran is running as intended.
    @JvmField val DISABLE_REMAPPING = "sporran.noRemap".checkPropertyBoolean()

    // Mainly for debugging, to make sure all Forge mods remap correctly in production environments
    // without needing to actually launch a production environment.
    @JvmField val FORCE_PRODUCTION_REMAPPING = FORCE_REMAPPING && "sporran.forceProductionRemap".checkPropertyBoolean()

    // Disables coremods in all loaded Forge mods.
    @JvmField val DISABLE_COREMODS = !"sporran.disableCoreMods".checkPropertyBoolean()

    // Stores modified coremods into the .sporran/modifiedCoreMods directory
    @JvmField val STORE_MODIFIED_COREMODS = "sporran.storeModifiedCoreMods".checkPropertyBoolean()

    // Mainly for debugging, enables profiling if the DeltaTimeProfiler#dumpTree method is called.
    // No longer does anything.
    //@JvmField val ENABLE_PROFILING = "sporran.enableProfiling".checkPropertyBoolean()

    // Mainly for debugging, enables logging access transformer info under the INFO level.
    // By default, AT info is logged under the DEBUG level, so it may still be found there.
    @JvmField val ENABLE_ACCESS_TRANSFORMER_DEBUG = "sporran.printATDebug".checkPropertyBoolean()

    // Some Forge mods call a System.exit if Sporran is present.
    // Sporran has a fixer that wraps System.exit to ensure users know why their game crashed, and additionally so it can
    // be tested in development. This flag allows the mod to be loaded anyway, completely overriding their code.
    @JvmField val DISABLE_FORGE_SYSTEM_EXIT = "sporran.disableSystemExit".checkPropertyBoolean()

    // Forcefully allow blocked mods to load in Sporran.
    // These mods may cause issues with Sporran, so here be dragons!
    @JvmField val FORCE_ALLOW_BLOCKED_MODS = "sporran.allowBlockedMods".checkPropertyBoolean()

    // Mainly for people who want to decompile Forge mods for themselves, and don't want to manually remap all SRG names.
    // This essentially disables most of Sporran's fixers that modify the mod's bytecode to make the mod work in Fabric.
    @JvmField val DISABLE_FIXERS = FORCE_REMAPPING && "sporran.disableFixers".checkPropertyBoolean()

    // Sporran: logs the startup phase timings (dev.sporran.util.SporranTimings) under the INFO level instead of DEBUG.
    @JvmField val PRINT_TIMINGS = "sporran.printTimings".checkPropertyBoolean()

    private fun String.checkPropertyBoolean(): Boolean {
        return System.getProperty(this)?.lowercase() == "true"
    }
}