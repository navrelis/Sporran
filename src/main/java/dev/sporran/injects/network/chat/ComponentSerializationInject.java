/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.network.chat;

import java.util.ArrayList;
import java.util.List;

import net.neoforged.neoforge.common.util.InsertingContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.ComponentSerialization;

@Mixin(ComponentSerialization.class)
public abstract class ComponentSerializationInject {
    @ModifyVariable(method = "createCodec", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/ComponentSerialization;createLegacyComponentMatcher([Lnet/minecraft/util/StringRepresentable;Ljava/util/function/Function;Ljava/util/function/Function;Ljava/lang/String;)Lcom/mojang/serialization/MapCodec;"))
    private static ComponentContents.Type<?>[] sporran$appendNeoContentToType(ComponentContents.Type<?>[] original) {
        List<ComponentContents.Type<?>> types = new ArrayList<>(List.of(original));
        types.add(InsertingContents.TYPE);
        return types.toArray(new ComponentContents.Type[0]);
    }
}
