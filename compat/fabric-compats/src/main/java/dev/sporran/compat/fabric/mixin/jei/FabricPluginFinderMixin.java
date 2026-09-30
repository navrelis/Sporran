package dev.sporran.compat.fabric.mixin.jei;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.fabric.startup.FabricPluginFinder;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.objectweb.asm.Type;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import dev.sporran.Sporran;
import dev.sporran.loader.SporranLoader;
import dev.sporran.loader.mod.NeoForgeMod;

import java.util.*;

@IfModLoaded("jei")
@Pseudo
@Mixin(value = FabricPluginFinder.class, remap = false)
public abstract class FabricPluginFinderMixin {
    @Unique private static final Type sporran$entrypointType = Type.getType(JeiPlugin.class);
    @Unique private static final Map<String, IModPlugin> sporran$loadedPluginInstances = Collections.synchronizedMap(new HashMap<>());

    @ModifyReturnValue(method = "getModPlugins", at = @At("RETURN"))
    private static List<IModPlugin> sporran$jei$appendForgeJEIPlugins(List<IModPlugin> original) {
        var launcher = FabricLauncherBase.getLauncher();

        for (NeoForgeMod mod : SporranLoader.Companion.getInstance().getMods()) {
            for (ModFileScanData.AnnotationData annotation : mod.getScanData().getAnnotations()) {
                if (annotation.annotationType().equals(sporran$entrypointType)) {
                    try {
                        if (sporran$loadedPluginInstances.containsKey(annotation.clazz().getClassName())) {
                            original.add(sporran$loadedPluginInstances.get(annotation.clazz().getClassName()));
                        } else {
                            var clazz = launcher.loadIntoTarget(annotation.clazz().getClassName());
                            var constructor = clazz.getDeclaredConstructor();
                            var value = (IModPlugin) constructor.newInstance();

                            sporran$loadedPluginInstances.put(annotation.clazz().getClassName(), value);

                            original.add(value);
                        }
                    } catch (Throwable e) {
                        Sporran.Companion.getLogger().error("Failed to register Forge JEI entrypoint {} for mod {} ({})!", annotation.clazz().getClassName(), mod.getDisplayName(), mod.getModId());
                        e.printStackTrace();
                    }
                }
            }
        }

        return original;
    }
}
