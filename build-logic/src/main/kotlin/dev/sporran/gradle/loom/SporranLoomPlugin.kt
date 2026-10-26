package dev.sporran.gradle.loom

import net.fabricmc.loom.api.LoomGradleExtensionAPI
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginAware

class SporranLoomPlugin : Plugin<PluginAware> {
    override fun apply(target: PluginAware) {
        if (target is Project)
            applyProject(target)
    }

    fun applyProject(project: Project) {
        project.logger.lifecycle("Applying Sporran Jar transformers")

        val loomExtension = project.extensions.getByName("loom") as LoomGradleExtensionAPI

//        loomExtension.addMinecraftJarProcessor(AccessTransformerProcessor::class.java)
    }
}
