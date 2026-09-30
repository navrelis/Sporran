package dev.sporran.compat.fabric

import com.moulberry.mixinconstraints.MixinConstraints
import net.fabricmc.loader.api.FabricLoader
import org.objectweb.asm.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo
import dev.sporran.api.compatibility.SporranModCompatBridgeManager
import dev.sporran.helpers.mixin.MixinExtensionHelper
import dev.sporran.loader.SporranLoader

class SporranFabricCompatsMixinPlugin : IMixinConfigPlugin {
    lateinit var mixinPackage: String

    override fun onLoad(mixinPackage: String) {
        this.mixinPackage = mixinPackage
    }

    override fun getRefMapperConfig(): String? {
        return null
    }

    override fun shouldApplyMixin(targetClassName: String, mixinClassName: String): Boolean {
        val modId = mixinClassName.removePrefix("$mixinPackage.").replaceAfter(".", "").removeSuffix(".")

        if (modId == "sophisticatedcore" || modId == "creativecore") {
            return FabricLoader.getInstance().isModLoaded(modId) && !SporranLoader.instance.hasMod(modId) && MixinConstraints.shouldApplyMixin(targetClassName, mixinClassName)
        }

        if (modId == "everycompat") {
            return SporranModCompatBridgeManager.isActive("everycomp") && MixinConstraints.shouldApplyMixin(targetClassName, mixinClassName)
        }

        if (modId == "accessories") {
            return FabricLoader.getInstance().isModLoaded("accessories")
                    && !SporranLoader.instance.hasMod("accessories")
                    && SporranLoader.instance.hasMod("cclayer")
        }

        if (modId == "cctweaked") {
            return SporranLoader.instance.hasMod("computercraft")
        }

        if (modId == "jade_forge") {
            return SporranLoader.instance.hasMod("jade")
        }

        return MixinConstraints.shouldApplyMixin(targetClassName, mixinClassName)
    }

    override fun acceptTargets(
        myTargets: Set<String?>?,
        otherTargets: Set<String?>?
    ) {
    }

    override fun getMixins(): List<String?>? {
        return null
    }

    override fun preApply(
        targetClassName: String?,
        targetClass: ClassNode?,
        mixinClassName: String?,
        mixinInfo: IMixinInfo?
    ) {
        MixinExtensionHelper.preApply(targetClassName, targetClass, mixinClassName, mixinInfo)
    }

    override fun postApply(
        targetClassName: String?,
        targetClass: ClassNode?,
        mixinClassName: String?,
        mixinInfo: IMixinInfo?
    ) {
        MixinExtensionHelper.postApply(targetClassName, targetClass, mixinClassName, mixinInfo)
    }
}
