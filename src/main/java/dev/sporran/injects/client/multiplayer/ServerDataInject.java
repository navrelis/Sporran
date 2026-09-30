// TRACKED HASH: bf31bbd65e367503d625d78fb5ab05a08980ceb2
package dev.sporran.injects.client.multiplayer;

import net.minecraft.client.multiplayer.ServerData;
import net.neoforged.neoforge.client.ExtendedServerListData;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.injections.client.multiplayer.ServerDataInjection;

@Mixin(ServerData.class)
public class ServerDataInject implements ServerDataInjection {
    public ExtendedServerListData neoForgeData = null;

    @Override
    public ExtendedServerListData sporran$getNeoForgeData() {
        return neoForgeData;
    }

    @Override
    public void sporran$setNeoForgeData(ExtendedServerListData data) {
        neoForgeData = data;
    }
}