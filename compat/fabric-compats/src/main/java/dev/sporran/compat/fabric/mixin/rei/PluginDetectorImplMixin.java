package dev.sporran.compat.fabric.mixin.rei;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import me.shedaniel.rei.api.common.plugins.REIPlugin;
import me.shedaniel.rei.api.common.plugins.REIPluginProvider;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import me.shedaniel.rei.fabric.PluginDetectorImpl;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.objectweb.asm.Type;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dev.sporran.Sporran;
import dev.sporran.compat.fabric.rei.SporranREIPluginProvider;
import dev.sporran.loader.SporranLoader;
import dev.sporran.loader.mod.NeoForgeMod;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@IfModLoaded("roughlyenoughitems")
@Pseudo
@Mixin(value = PluginDetectorImpl.class, remap = false)
public abstract class PluginDetectorImplMixin {
    @Unique private static final Type sporran$clientEntrypointType = Type.getType("Lme/shedaniel/rei/forge/REIPluginClient;");
    @Unique private static final Type sporran$serverEntrypointType = Type.getType("Lme/shedaniel/rei/forge/REIPluginDedicatedServer;");
    @Unique private static final Type sporran$commonEntrypointType = Type.getType("Lme/shedaniel/rei/forge/REIPluginCommon;");
    @Unique private static final Map<String, REIPluginProvider<?>> sporran$loadedPluginInstances = Collections.synchronizedMap(new HashMap<>());

    @Inject(method = "loadPlugin", at = @At("TAIL"))
    private static <P extends REIPlugin<?>> void sporran$rei$loadForgeREIPlugins(Class<? extends P> pluginClass, Consumer<? super REIPluginProvider<P>> consumer, CallbackInfo ci) {
        Type entrypointType;

        if (pluginClass == REIServerPlugin.class)
            entrypointType = sporran$serverEntrypointType;
        else if (pluginClass == (Class<? extends REIPlugin<?>>) (Class) REIPlugin.class)
            entrypointType = sporran$commonEntrypointType;
        else if (pluginClass.getSimpleName().equals("REIClientPlugin")) // If we try to load this, it'll cause a crash on servers.
            entrypointType = sporran$clientEntrypointType;
        else return;

        var launcher = FabricLauncherBase.getLauncher();

        for (NeoForgeMod mod : SporranLoader.Companion.getInstance().getMods()) {
            for (ModFileScanData.AnnotationData annotation : mod.getScanData().getAnnotations()) {
                if (annotation.annotationType().equals(entrypointType)) {
                    try {
                        REIPluginProvider<P> plugin;

                        var clazz = launcher.loadIntoTarget(annotation.clazz().getClassName());
                        if (sporran$loadedPluginInstances.containsKey(annotation.clazz().getClassName())) {
                            var constructor = clazz.getDeclaredConstructor();
                            plugin = (REIPluginProvider<P>) constructor.newInstance();
                        } else {
                            plugin = (REIPluginProvider<P>) sporran$loadedPluginInstances.get(annotation.clazz().getClassName());
                        }

                        if (pluginClass.isAssignableFrom(plugin.getPluginProviderClass())) {
                            consumer.accept(new SporranREIPluginProvider<>(plugin, mod));
                        }
                    } catch (Throwable e) {
                        Sporran.Companion.getLogger().error("Failed to register Forge REI entrypoint {} for mod {} ({})!", annotation.clazz().getClassName(), mod.getDisplayName(), mod.getModId());
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
