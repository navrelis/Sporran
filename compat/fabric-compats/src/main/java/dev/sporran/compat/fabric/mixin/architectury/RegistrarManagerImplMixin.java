package dev.sporran.compat.fabric.mixin.architectury;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import dev.architectury.registry.registries.fabric.RegistrarManagerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import dev.sporran.compat.fabric.architectury.SporranArchitecturyApiCompat;
import dev.sporran.loader.SporranLoader;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

@IfModLoaded("architectury")
@Pseudo
@Mixin(RegistrarManagerImpl.RegistrarImpl.class)
public abstract class RegistrarManagerImplMixin<T> {
    @Shadow
    private Registry<T> delegate;

    @WrapOperation(method = "register", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Registry;register(Lnet/minecraft/core/Registry;Lnet/minecraft/resources/ResourceLocation;Ljava/lang/Object;)Ljava/lang/Object;"))
    private T sporran$delayRegisterIfNeeded(Registry<T> registry, ResourceLocation name, T value, Operation<T> original) {
        // Defer to Sporran for registration, because deferred registry sucks.
        if (SporranLoader.Companion.getInstance().hasMod(name.getNamespace())) {
            SporranArchitecturyApiCompat.delayForRegisterEvent(delegate, () -> original.call(registry, name, value));
            return value;
        }

        return original.call(registry, name, value);
    }

//    @Shadow private Registry<T> delegate;
//
//    @Unique private Set<Integer> sporran$registeredIds;
//    @Unique private Set<ResourceLocation> sporran$registeredKeys = new HashSet<>();
//    @Unique private Set<T> sporran$registeredValues = new HashSet<>();
//
//    @Inject(method = "<init>", at = @At("TAIL"))
//    private void sporran$architectury$detectRegisterEvents(String modId, Registry<T> delegate, CallbackInfo ci) {
//        var ids = this.sporran$registeredIds = new HashSet<>();
//        var keys = this.sporran$registeredKeys = new HashSet<>();
//        var values = this.sporran$registeredValues = new HashSet<>();
//
//        var event = RegistryEntryAddedCallback.event(delegate);
//        var earlyId = ResourceLocation.fromNamespaceAndPath(Sporran.MOD_ID, "early");
//        event.addPhaseOrdering(earlyId, Event.DEFAULT_PHASE);
//        event.register(earlyId, (id, key, value) -> {
//                ids.add(id);
//                keys.add(key);
//                values.add(value);
//            });
//    }
//
//    // FIXME: This is very much a workaround but idk how else to fix this.
//    //        The main issue is that Sporran is somehow preventing Fabric's RegistryEntryAdded callback from running, which causes issues with Architectury's spawn egg items especially.
//    @ModifyExpressionValue(method = "register", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Registry;register(Lnet/minecraft/core/Registry;Lnet/minecraft/resources/ResourceLocation;Ljava/lang/Object;)Ljava/lang/Object;"))
//    private T sporran$architectury$forceAddRegister(T original) {
//        var id = delegate.getId(original);
//        var key = delegate.getKey(original);
//
//        if (!(this.sporran$registeredIds.contains(id) && this.sporran$registeredKeys.contains(key) && this.sporran$registeredValues.contains(original))) {
//            RegistryEntryAddedCallback.event(delegate).invoker().onEntryAdded(id, key, original);
//        }
//
//        return original;
//    }
}
