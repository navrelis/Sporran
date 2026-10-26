/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.mixin.world.item;

import net.minecraft.world.item.UseAnim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(UseAnim.class)
public interface UseAnimAccessor {
    @Invoker("<init>")
    static UseAnim createUseAnim(String name, int size) {
        throw new IllegalStateException();
    }

    @Accessor("$VALUES")
    static UseAnim[] getValues() {
        throw new IllegalStateException();
    }

    @Mutable
    @Accessor("$VALUES")
    static void setValues(UseAnim[] values) {
        throw new IllegalStateException();
    }
}
