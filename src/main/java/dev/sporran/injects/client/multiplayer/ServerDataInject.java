// TRACKED HASH: bf31bbd65e367503d625d78fb5ab05a08980ceb2
/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

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