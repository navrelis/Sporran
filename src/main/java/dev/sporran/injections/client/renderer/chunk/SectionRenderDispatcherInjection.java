/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.client.renderer.chunk;

import java.util.List;

import net.neoforged.neoforge.client.event.AddSectionGeometryEvent;
import dev.sporran.util.SporranHelper;

public interface SectionRenderDispatcherInjection {
    interface RenderSectionInjection {
        interface RebuildTaskInjection {
            default void sporran$setAdditionalRenderers(List<AddSectionGeometryEvent.AdditionalSectionRenderer> renderers) {
                throw SporranHelper.createMixinException(RebuildTaskInjection.class, "sporran$setAdditionalRenderers");
            }
        }
    }
}
