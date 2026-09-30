// TRACKED HASH: f02e766e5f7ff541845462963634270b691f3cc9
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