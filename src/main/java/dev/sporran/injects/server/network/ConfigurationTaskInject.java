/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.server.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.network.ConfigurationTask;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.helpers.mixin.CreateInitializer;

@Mixin(ConfigurationTask.class)
public interface ConfigurationTaskInject {
    @Mixin(ConfigurationTask.Type.class)
    abstract class TypeInject {
        public TypeInject(String id) {}

        @CreateInitializer
        public TypeInject(ResourceLocation id) {
            this(id.toString());
        }
    }
}
