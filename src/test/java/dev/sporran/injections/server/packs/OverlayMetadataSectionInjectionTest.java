package dev.sporran.injections.server.packs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.minecraft.server.packs.AbstractPackResources;

/**
 * Packs that declare {@code fabric:overlays} (Lithostitched 1.8.1 declares both lists) get their overlays from Fabric
 * API. Sporran must not add their {@code neoforge:overlays} on top: the conditions of those entries are Fabric ones
 * ({@code apollib:config}), which Sporran's condition codec turns into "true", so overlays the author switched off by
 * config (the debug suite with its armadillo spawns) were always applied.
 * <p>
 * Plain unit test: only the metadata section type and the pure helper are used, no game bootstrap.
 */
public class OverlayMetadataSectionInjectionTest {
    private static final List<String> NEO = List.of("overlay.breaks_seed_parity", "overlay.debug_suite");

    /** pack.mcmeta of Lithostitched 1.8.1. */
    private static final String LITHOSTITCHED = """
            {
              "pack": {"description": "Lithostitched resources", "pack_format": 48, "supported_formats": [48, 999], "min_format": 48, "max_format": 999},
              "neoforge:overlays": {"entries": [
                {"directory": "overlay.breaks_seed_parity", "formats": [48, 999], "min_format": 48, "max_format": 999,
                 "neoforge:conditions": [{"type": "apollib:config", "key": "lithostitched:breaks_seed_parity"}]},
                {"directory": "overlay.debug_suite", "formats": [48, 999], "min_format": 48, "max_format": 999,
                 "neoforge:conditions": [{"type": "apollib:config", "key": "lithostitched:debug_suite"}]}]},
              "fabric:overlays": {"entries": [
                {"directory": "overlay.breaks_seed_parity", "condition": {"condition": "apollib:config", "key": "lithostitched:breaks_seed_parity"}},
                {"directory": "overlay.debug_suite", "condition": {"condition": "apollib:config", "key": "lithostitched:debug_suite"}}]}
            }
            """;

    private static Object readFabricSection(String json) {
        return AbstractPackResources.getMetadataFromStream(OverlayMetadataSectionInjection.FABRIC_TYPE,
                new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void packWithFabricOverlaysGetsNoNeoForgeOverlays() {
        assertEquals(List.of(), OverlayMetadataSectionInjection.neoForgeOverlaysToAdd(true, NEO));
    }

    @Test
    void packWithoutFabricOverlaysKeepsNeoForgeOverlays() {
        assertSame(NEO, OverlayMetadataSectionInjection.neoForgeOverlaysToAdd(false, NEO));
        assertEquals(NEO, OverlayMetadataSectionInjection.neoForgeOverlaysToAdd(false, NEO));
    }

    @Test
    void emptyListsStayEmpty() {
        assertEquals(List.of(), OverlayMetadataSectionInjection.neoForgeOverlaysToAdd(true, List.of()));
        assertEquals(List.of(), OverlayMetadataSectionInjection.neoForgeOverlaysToAdd(false, List.of()));
    }

    @Test
    void fabricSectionTypeUsesTheFabricSectionName() {
        assertEquals("fabric:overlays", OverlayMetadataSectionInjection.FABRIC_TYPE.getMetadataSectionName());
    }

    @Test
    void lithostitchedFabricSectionIsPresent() {
        assertNotNull(readFabricSection(LITHOSTITCHED));
    }

    @Test
    void anyJsonObjectInTheFabricSectionIsPresent() {
        assertNotNull(readFabricSection("{\"fabric:overlays\": {}}"));
        assertNotNull(readFabricSection("{\"fabric:overlays\": {\"entries\": []}}"));
        // malformed for Fabric API's own reader and a format Sporran has never seen: still counts as declared
        assertNotNull(readFabricSection("{\"fabric:overlays\": {\"entries\": \"nope\", \"future\": [1, {\"a\": null}]}}"));
        assertNotNull(readFabricSection("{\"fabric:overlays\": {\"entries\": [{\"directory\": 5}]}}"));
    }

    @Test
    void missingFabricSectionIsAbsent() {
        assertNull(readFabricSection("{\"pack\": {\"description\": \"x\", \"pack_format\": 48}}"));
        assertNull(readFabricSection("{\"neoforge:overlays\": {\"entries\": []}}"));
    }

    @Test
    void nonObjectFabricSectionIsAbsentBecauseVanillaOnlyHandsObjectsToTheCodec() {
        // AbstractPackResources.getMetadataFromStream calls GsonHelper.getAsJsonObject before the codec runs, so an array
        // or a string is rejected there (logged as an error) for every section type, Fabric API's own reader included.
        assertNull(readFabricSection("{\"fabric:overlays\": []}"));
        assertNull(readFabricSection("{\"fabric:overlays\": \"x\"}"));
    }
}
