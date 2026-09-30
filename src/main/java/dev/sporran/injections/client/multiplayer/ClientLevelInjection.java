package dev.sporran.injections.client.multiplayer;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.neoforged.neoforge.entity.PartEntity;
import dev.sporran.util.SporranHelper;

public interface ClientLevelInjection {
    default Int2ObjectMap<PartEntity<?>> sporran$getPartEntitiesMap() {
        throw SporranHelper.createMixinException(ClientLevelInjection.class, "sporran$getPartEntitiesMap");
    }
}
