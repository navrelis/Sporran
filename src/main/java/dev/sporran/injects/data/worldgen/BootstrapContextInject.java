// TRACKED HASH: 50ccda2541c46785a0be46cf552b476164633abf
package dev.sporran.injects.data.worldgen;

import net.minecraft.data.worldgen.BootstrapContext;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.injections.data.worldgen.BootstrapContextInjection;

@Mixin(BootstrapContext.class)
public interface BootstrapContextInject extends BootstrapContextInjection {
}