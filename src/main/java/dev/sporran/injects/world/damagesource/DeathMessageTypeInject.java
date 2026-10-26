/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.damagesource;

import net.minecraft.world.damagesource.DeathMessageType;
import net.neoforged.fml.common.asm.enumextension.*;
import net.neoforged.neoforge.common.damagesource.IDeathMessageProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dev.sporran.helpers.mixin.CreateInitializer;
import dev.sporran.helpers.mixin.CreateStatic;
import dev.sporran.injections.world.damagesource.DeathMessageTypeInjection;

@NamedEnum
@NetworkedEnum(NetworkedEnum.NetworkCheck.CLIENTBOUND)
@Mixin(DeathMessageType.class)
public abstract class DeathMessageTypeInject implements IExtensibleEnum, DeathMessageTypeInjection {
    @Unique
    private IDeathMessageProvider msgFunction = IDeathMessageProvider.DEFAULT;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void sporran$initDefaultMessageFunction(String string, int i, String id, CallbackInfo ci) {
        this.msgFunction = IDeathMessageProvider.DEFAULT;
    }

    @ReservedConstructor
    private DeathMessageTypeInject(String name, int ordinal, String id) {}

    @CreateInitializer
    private DeathMessageTypeInject(String name, int ordinal, String id, IDeathMessageProvider msgFunction) {
        this(name, ordinal, id);
        this.msgFunction = msgFunction;
    }

    @Override
    public IDeathMessageProvider getMessageFunction() {
        return this.msgFunction;
    }

    @CreateStatic
    private static ExtensionInfo getExtensionInfo() {
        return ExtensionInfo.nonExtended(DeathMessageType.class);
    }
}
