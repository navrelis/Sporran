/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.compat.forge

import com.moulberry.mixinconstraints.MixinConstraints
import org.objectweb.asm.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo
import dev.sporran.loader.SporranLoader

class SporranForgeCompatMixinPlugin : IMixinConfigPlugin {
    lateinit var mixinPackage: String

    override fun onLoad(mixinPackage: String) {
        this.mixinPackage = mixinPackage
    }

    override fun getRefMapperConfig(): String? {
        return null
    }

    override fun shouldApplyMixin(targetClassName: String?, mixinClassName: String): Boolean {
        return try {
            val modId = mixinClassName.removePrefix("$mixinPackage.").split(".")[0]
            SporranLoader.instance.hasMod(modId) && MixinConstraints.shouldApplyMixin(mixinClassName)
        } catch (_: Throwable) {
            MixinConstraints.shouldApplyMixin(mixinClassName)
        }
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
    }

    override fun postApply(
        targetClassName: String?,
        targetClass: ClassNode?,
        mixinClassName: String?,
        mixinInfo: IMixinInfo?
    ) {
    }
}