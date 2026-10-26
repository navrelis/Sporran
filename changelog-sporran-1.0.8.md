## Sporran 1.0.8

One fix.

- **Fixed: Fabric mods that carry both Fabric and NeoForge overlay lists had their NeoForge overlays applied on top.**
  Lithostitched 1.8.1 is one of them: its Fabric overlay list switches the debug suite and the seed-parity overlay on
  only when the mod's config asks for it, but Sporran also read the NeoForge list, could not understand the Fabric
  config condition there and treated it as "true". The debug suite was therefore always on: armadillos in every
  Overworld biome, sky islands and more. Sporran now leaves the overlays of a pack that declares `fabric:overlays` to
  Fabric API and its conditions. Chunks that were generated before this fix keep what was generated.
