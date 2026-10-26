/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.nbt;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.StringTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import dev.sporran.injections.nbt.NbtAccounterInjection;

@Mixin(StringTag.class)
public abstract class StringTagInject {
    @Mixin(targets = "net/minecraft/nbt/StringTag$1")
    public static abstract class Inner1Inject {
        /*@Redirect(method = "load(Ljava/io/DataInput;ILnet/minecraft/nbt/NbtAccounter;)Lnet/minecraft/nbt/StringTag;", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtAccounter;accountBytes(J)V", ordinal = 1))
        private void sporran$useImprovedUTFAccounter(NbtAccounter instance, long bytes, @Local String string) {
            ((NbtAccounterInjection) instance).readUTF(string);
        }*/
    }
}
