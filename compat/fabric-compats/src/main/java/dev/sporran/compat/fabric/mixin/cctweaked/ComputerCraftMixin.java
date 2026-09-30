package dev.sporran.compat.fabric.mixin.cctweaked;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.ForgeComputerCraftAPI;
import dan200.computercraft.shared.ComputerCraft;
import dan200.computercraft.shared.integration.CreateIntegration;
import dan200.computercraft.shared.peripheral.generic.methods.EnergyMethods;
import dan200.computercraft.shared.peripheral.generic.methods.FluidMethods;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dev.sporran.loader.SporranLoader;

@Mixin(ComputerCraft.class)
public abstract class ComputerCraftMixin {
    @Inject(method = "init", at = @At("TAIL"))
    private static void sporran$addCreateCompat(CallbackInfo ci) {
        ComputerCraftAPI.registerGenericSource(new FluidMethods());
        ComputerCraftAPI.registerGenericSource(new EnergyMethods());

        ForgeComputerCraftAPI.registerGenericCapability(Capabilities.FluidHandler.BLOCK);
        ForgeComputerCraftAPI.registerGenericCapability(Capabilities.EnergyStorage.BLOCK);

        if (SporranLoader.Companion.getInstance().hasMod("create")) {
            CreateIntegration.setup();
        }
    }
}
