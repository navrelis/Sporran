package dev.sporran.compat.fabric.mixin.emi;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.platform.fabric.EmiAgnosFabric;
import dev.emi.emi.registry.EmiPluginContainer;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IfModLoaded("emi")
@Pseudo
@Mixin(value = EmiAgnosFabric.class, remap = false)
public abstract class EmiAgnosFabricMixin {
    @Unique private static final Type sporran$entrypointType = Type.getType(EmiEntrypoint.class);
    @Unique private static final Map<String, EmiPlugin> sporran$loadedPluginInstances = new HashMap<>();

    @ModifyReturnValue(method = "getModsWithPluginsAgnos", at = @At("RETURN"))
    private List<String> sporran$emi$appendForgeEMIPluginIds(List<String> original) {
        for (NeoForgeMod mod : SporranLoader.Companion.getInstance().getMods()) {
            for (ModFileScanData.AnnotationData annotation : mod.getScanData().getAnnotations()) {
                if (annotation.annotationType().equals(sporran$entrypointType)) {
                    original.add(mod.getModId());

                    break;
                }
            }
        }

        return original;
    }

    @ModifyReturnValue(method = "getPluginsAgnos", at = @At("RETURN"))
    private List<EmiPluginContainer> sporran$emi$appendForgeEMIPlugins(List<EmiPluginContainer> original) {
        var launcher = FabricLauncherBase.getLauncher();

        for (NeoForgeMod mod : SporranLoader.Companion.getInstance().getMods()) {
            for (ModFileScanData.AnnotationData annotation : mod.getScanData().getAnnotations()) {
                if (annotation.annotationType().equals(sporran$entrypointType)) {
                    try {
                        if (sporran$loadedPluginInstances.containsKey(annotation.clazz().getClassName())) {
                            original.add(new EmiPluginContainer(sporran$loadedPluginInstances.get(annotation.clazz().getClassName()), mod.getModId()));
                        } else {
                            var clazz = launcher.loadIntoTarget(annotation.clazz().getClassName());
                            var constructor = clazz.getDeclaredConstructor();
                            var value = (EmiPlugin) constructor.newInstance();

                            sporran$loadedPluginInstances.put(annotation.clazz().getClassName(), value);

                            original.add(new EmiPluginContainer(value, mod.getModId()));
                        }
                    } catch (Throwable e) {
                        Sporran.Companion.getLogger().error("Failed to register Forge EMI entrypoint {} for mod {} ({})!", annotation.clazz().getClassName(), mod.getDisplayName(), mod.getModId());
                        e.printStackTrace();
                    }
                }
            }
        }

        return original;
    }
}
