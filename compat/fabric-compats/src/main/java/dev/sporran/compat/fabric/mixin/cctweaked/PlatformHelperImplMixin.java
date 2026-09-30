package dev.sporran.compat.fabric.mixin.cctweaked;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import dan200.computercraft.api.media.IMedia;
import dan200.computercraft.api.media.MediaCapability;
import dan200.computercraft.api.network.wired.WiredElement;
import dan200.computercraft.api.network.wired.WiredElementCapability;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.PeripheralCapability;
import dan200.computercraft.shared.platform.ComponentAccess;
import dan200.computercraft.shared.platform.PlatformHelperImpl;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import dev.sporran.compat.fabric.cctweaked.ComponentAccessImplExt;

import java.util.function.Consumer;

@Mixin(PlatformHelperImpl.class)
public abstract class PlatformHelperImplMixin {
    @ModifyReturnValue(method = "createWiredElementAccess", at = @At("RETURN"))
    public ComponentAccess<WiredElement> sporran$addWiredElementCapabilities(ComponentAccess<WiredElement> original, @Local(argsOnly = true, name = "invalidate") Consumer<Direction> invalidate) {
        if (original instanceof ComponentAccessImplExt ext) {
            ext.sporran$initializeCapabilityLookups(WiredElementCapability.get(), invalidate);
        }
        return original;
    }

    @ModifyReturnValue(method = "createPeripheralAccess", at = @At("RETURN"))
    public ComponentAccess<IPeripheral> sporran$addPeripheralCapabilities(ComponentAccess<IPeripheral> original, @Local(argsOnly = true, name = "invalidate") Consumer<Direction> invalidate) {
        if (original instanceof ComponentAccessImplExt ext) {
            ext.sporran$initializeCapabilityLookups(PeripheralCapability.get(), invalidate);
        }
        return original;
    }


    @ModifyReturnValue(method = "getMedia", at = @At("RETURN"))
    public IMedia sporran$trySearchCapability(IMedia original, @Local(argsOnly = true, name = "stack") ItemStack stack) {
        if (original != null) {
            return original;
        }
        return stack.getCapability(MediaCapability.get());
    }
}
