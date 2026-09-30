package dev.sporran.injects.world.level.chunk;

import net.minecraft.world.level.chunk.PalettedContainer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PalettedContainer.class)
public abstract class PalettedContainerInject {
    // Sporran: Funniest shit? There's no patch here.
    //       There's just a to-do here from Forge.
}
