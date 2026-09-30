package dev.sporran.mixin.workarounds.mapped_registry_aliases;

import net.fabricmc.fabric.api.event.registry.FabricRegistry;
import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.BaseMappedRegistry;
import org.spongepowered.asm.mixin.*;
import dev.sporran.loader.SporranLoader;
import dev.sporran.workarounds.MappedRegistryWorkaround;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

@SuppressWarnings("NonExtendableApiUsage")
@Mixin(value = MappedRegistry.class, priority = 1050)
@Implements(@Interface(iface = MappedRegistryWorkaround.class, prefix = "sporran$i$"))
public abstract class MappedRegistryMixin implements FabricRegistry {

    @Unique
    private static final MethodHandle sporran$super$addAlias;

    static {
        try {
            // https://stackoverflow.com/a/15674467
            //noinspection JavaLangInvokeHandleSignature
            sporran$super$addAlias = MethodHandles.lookup().findSpecial(
                BaseMappedRegistry.class, "addAlias",
                MethodType.methodType(Void.TYPE, ResourceLocation.class, ResourceLocation.class),
                MappedRegistry.class
            );
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Intrinsic(displace = true)
    public void sporran$i$addAlias(ResourceLocation old, ResourceLocation newId) {
        if (SporranLoader.Companion.getInstance().hasMod(old.getNamespace())) {
            try {
                //noinspection JavaLangInvokeHandleSignature
                sporran$super$addAlias.invoke(this, old, newId);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        } else {
            addAlias(old, newId);
        }
    }

}
