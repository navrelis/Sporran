package dev.sporran.injects.client.multiplayer;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.multiplayer.KnownPacksManager;

@Mixin(KnownPacksManager.class)
public abstract class KnownPacksManagerInject {
    // Sporran: don't think we need to do anything about this?
}
