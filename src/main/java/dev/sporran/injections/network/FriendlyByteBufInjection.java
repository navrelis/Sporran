/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.ApiStatus;
import dev.sporran.processor.FabricInjectedInterface;
import dev.sporran.util.SporranHelper;

@FabricInjectedInterface(FriendlyByteBuf.class)
public interface FriendlyByteBufInjection {
    @ApiStatus.Internal
    default ByteBuf getSource() {
        throw SporranHelper.createMixinException(FriendlyByteBufInjection.class, "getSource");
    }
}
