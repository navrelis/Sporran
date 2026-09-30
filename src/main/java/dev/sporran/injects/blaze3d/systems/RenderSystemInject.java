package dev.sporran.injects.blaze3d.systems;

import com.mojang.blaze3d.systems.RenderSystem;
import net.neoforged.neoforge.client.GlStateBackup;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.helpers.mixin.CreateStatic;
import dev.sporran.injections.blaze3d.systems.RenderSystemInjection;

@Mixin(RenderSystem.class)
public abstract class RenderSystemInject {
    @CreateStatic
    private static void backupGlState(GlStateBackup state) {
        RenderSystemInjection.backupGlState(state);
    }

    @CreateStatic
    private static void restoreGlState(GlStateBackup state) {
        RenderSystemInjection.restoreGlState(state);
    }
}
