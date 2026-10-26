/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.gametest.framework;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.gametest.framework.TestCommand;
import net.minecraft.world.level.block.entity.StructureBlockEntity;

@Mixin(TestCommand.class)
public abstract class TestCommandInject {
    @WrapOperation(method = "showPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/StructureBlockEntity;getMetaData()Ljava/lang/String;"))
    private static String sporran$tryUseStructureName(StructureBlockEntity instance, Operation<String> original) {
        return original.call(instance).isBlank() ? instance.getStructureName() : original.call(instance);
    }
}
