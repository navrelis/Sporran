/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.client;

import net.minecraft.client.MouseHandler;
import dev.sporran.processor.FabricInjectedInterface;

@FabricInjectedInterface(MouseHandler.class)
public interface MouseHandlerInjection {
    default double getXVelocity() {
        throw new IllegalStateException();
    }

    default double getYVelocity() {
        throw new IllegalStateException();
    }
}
