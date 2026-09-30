package dev.sporran.compat.fabric.mixin.resourcefullib;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import java.util.function.Supplier;

import com.teamresourceful.resourcefullib.common.registry.HolderRegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.fabric.FabricResourcefulRegistry;
import com.teamresourceful.resourcefullib.common.registry.neoforge.NeoForgeHolderRegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.neoforge.NeoForgeRegistryEntry;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import dev.sporran.Sporran;
import dev.sporran.api.compatibility.ModBridgeStrategy;
import dev.sporran.compat.fabric.resourcefullib.SporranResourcefulLibCompat;

import net.minecraft.core.Registry;

@IfModLoaded("resourcefullib")
@Pseudo
@Mixin(FabricResourcefulRegistry.class)
public abstract class FabricResourcefulRegistryMixin<T> {
    @Unique private DeferredRegister<T> sporran$deferredRegister;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void sporran$setupDeferredRegisterIfPossible(Registry<T> registry, String id, CallbackInfo ci) {
        if (Sporran.Companion.getLoader().hasMod(id) && !ModBridgeStrategy.checkFabricExists(id)) {
            this.sporran$deferredRegister = DeferredRegister.create(registry.key(), id);
            SporranResourcefulLibCompat.attachToModContainer(id, this.sporran$deferredRegister);
        }
    }

    @Inject(method = "register", at = @At("HEAD"), cancellable = true)
    private <I extends T> void sporran$tryUseNeoEntryIfPossible(String id, Supplier<I> supplier, CallbackInfoReturnable<RegistryEntry<I>> cir) {
        if (this.sporran$deferredRegister != null) {
            cir.setReturnValue(new NeoForgeRegistryEntry<>(this.sporran$deferredRegister.register(id, supplier)));
        }
    }

    @Inject(method = "registerHolder", at = @At("HEAD"), cancellable = true)
    private void sporran$tryUseNeoHolderEntryIfPossible(String id, Supplier<T> supplier, CallbackInfoReturnable<HolderRegistryEntry<T>> cir) {
        if (this.sporran$deferredRegister != null) {
            cir.setReturnValue(new NeoForgeHolderRegistryEntry<>(this.sporran$deferredRegister.register(id, supplier)));
        }
    }
}
