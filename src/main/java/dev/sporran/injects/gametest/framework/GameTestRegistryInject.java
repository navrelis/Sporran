// TRACKED HASH: f02e766e5f7ff541845462963634270b691f3cc9
/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.gametest.framework;

import net.minecraft.gametest.framework.GameTestRegistry;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.helpers.mixin.CreateStatic;
import dev.sporran.injections.gametest.framework.GameTestRegistryInjection;

import java.lang.reflect.Method;
import java.util.Set;

@Mixin(GameTestRegistry.class)
public class GameTestRegistryInject implements GameTestRegistryInjection {
    @CreateStatic
    private static void register(Method method, Set<String> allowedNamespaces) {
        GameTestRegistryInjection.register(method, allowedNamespaces);
    }
}