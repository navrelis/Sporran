/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.level.block;

import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;
import dev.sporran.util.SporranHelper;

import java.util.function.Consumer;

public interface BlockInjection {
    default void initializeClient(Consumer<IClientBlockExtensions> consumer) {
        throw SporranHelper.createMixinException(BlockInjection.class, "initializeClient");
    }
}
