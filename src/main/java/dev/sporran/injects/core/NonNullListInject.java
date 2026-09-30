package dev.sporran.injects.core;

import com.mojang.serialization.Codec;
import net.minecraft.core.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.helpers.mixin.CreateStatic;
import dev.sporran.injections.core.NonNullListInjection;

import java.util.Collection;

@Mixin(NonNullList.class)
public abstract class NonNullListInject implements NonNullListInjection {
    @CreateStatic
    private static <E> Codec<NonNullList<E>> codecOf(Codec<E> entryCodec) {
        return NonNullListInjection.codecOf(entryCodec);
    }

    @CreateStatic
    private static <E> NonNullList<E> copyOf(Collection<? extends E> entries) {
        return NonNullListInjection.copyOf(entries);
    }
}
