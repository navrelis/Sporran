// TRACKED HASH: 65eab7af6923cfe40b811ec9f2b77f27d0284455
package dev.sporran.injects.network;

import io.netty.buffer.ByteBuf;
import net.neoforged.neoforge.common.extensions.IFriendlyByteBufExtension;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import dev.sporran.injections.network.FriendlyByteBufInjection;

import net.minecraft.network.FriendlyByteBuf;

@Mixin(FriendlyByteBuf.class)
public abstract class FriendlyByteBufInject implements IFriendlyByteBufExtension, FriendlyByteBufInjection {
    @Shadow @Final private ByteBuf source;

    // Sporran: Capacity size limiting implemented by Fabric API

    @Override
    public ByteBuf getSource() {
        return this.source;
    }
}
