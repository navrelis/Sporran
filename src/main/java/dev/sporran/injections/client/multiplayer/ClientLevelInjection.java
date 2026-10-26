/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.client.multiplayer;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.neoforged.neoforge.entity.PartEntity;
import dev.sporran.util.SporranHelper;

public interface ClientLevelInjection {
    default Int2ObjectMap<PartEntity<?>> sporran$getPartEntitiesMap() {
        throw SporranHelper.createMixinException(ClientLevelInjection.class, "sporran$getPartEntitiesMap");
    }
}
