/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.compat.fabric.rei

import me.shedaniel.rei.api.common.plugins.REIPlugin
import me.shedaniel.rei.api.common.plugins.REIPluginProvider
import dev.sporran.loader.mod.NeoForgeMod

class SporranREIPluginProvider<P : REIPlugin<*>>(private val plugin: REIPluginProvider<P>, private val mod: NeoForgeMod) : REIPluginProvider<P> {
    override fun provide(): Collection<P?>? {
        return plugin.provide()
    }

    override fun getPluginProviderClass(): Class<P?>? {
        return plugin.pluginProviderClass
    }

    override fun getPluginProviderName(): String? {
        return "${plugin.pluginProviderName} [${mod.definition.id}]"
    }
}