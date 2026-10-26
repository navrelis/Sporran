package dev.sporran.helpers;

import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import dev.sporran.Sporran;

import java.util.ArrayList;
import java.util.List;

/**
 * Sporran: data map types that Fabric mods register through NeoForge Data Pack Extensions (a Fabric port of NeoForge's
 * data maps, nested in Dyed Flames). With Sporran installed that library's own implementation is switched off (see
 * {@code NeoForgeDataPackExtensionsCompat}) and its types are handed to Sporran's NeoForge data maps instead, which load,
 * sync and look them up like any NeoForge mod's data maps.
 */
public final class ForeignDataMapTypes {
    private static final List<DataMapType<?, ?>> PENDING = new ArrayList<>();
    private static boolean collected = false;

    private ForeignDataMapTypes() {
    }

    public static synchronized void register(DataMapType<?, ?> type) {
        if (collected) {
            Sporran.Companion.getLogger().error("Sporran: data map type {} was registered after NeoForge collected the data map types, it will not be loaded", type.id());
            return;
        }

        PENDING.add(type);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static synchronized void registerAll(RegisterDataMapTypesEvent event) {
        collected = true;

        for (DataMapType type : PENDING) {
            event.register(type);
        }
    }
}
