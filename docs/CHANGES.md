# Changes in Sporran

Sporran is a modified version of Kilt v21.1.13 (https://github.com/KiltMC/Kilt, LGPL-2.1). As the LGPL requires, this
page records what Sporran changed compared to that release: the fixes to NeoForge API behaviour, the renaming, and
what is still known to be missing.

## Version 1.0.7

- Fixed: `IncompatibleClassChangeError: Conflicting default methods` on `getAppearance`. NeoForge's `IBlockExtension`
  declares `getAppearance(BlockState, BlockAndTintGetter, BlockPos, Direction, BlockState, BlockPos)` as a default
  method, and Sporran's `IBlockExtension` extends Fabric API's `FabricBlock`, which has a default method with the same
  name and descriptor. A block class that also gets `FabricBlock` through a third interface that overrides the method
  (FramedBlocks' `IFramedBlock`) therefore inherited two unrelated most-specific defaults, and the JVM threw on every
  call, for example when connected textures asked a framed block for its appearance. `IBlockExtension` no longer
  re-declares it: the method is inherited from `FabricBlock`, whose default also returns the given state, so NeoForge
  mods that call or override it (same name and descriptor) behave as before.

## Version 1.0.6

- Fixed: the spark profiler's mod-sources view. spark builds a table from every Fabric mod's root path to the mod id and
  stops on a path that two mods share. Sporran adds the container `neoforge`, whose root path is the Sporran jar, the
  same as Sporran's own, so spark logged "Multiple entries with same key" and its profiles had no per-mod "Sources"
  view. A compat mixin now leaves the repeated root path out of that table, so the first mod keeps it. Sporran's own
  containers are not changed.

## Version 1.0.5

- Fixed: the screen shake of NeoForge mods spun the picture instead of tilting it. A NeoForge mod can tilt the camera
  (the "roll") through `ViewportEvent.ComputeCameraAngles` and `Camera#setRotation(yaw, pitch, roll)`; it gives the
  roll in degrees, like yaw and pitch. Sporran handed that number on in degrees where the game expects radians (and
  with the sign the other way round): the roll was 57 times too strong. A boss slam that is meant to tilt the screen by
  a few degrees turned it by a full circle and more, many times a second, so that nothing could be seen. The roll is now
  converted as in NeoForge (`-roll * (PI / 180)`), so a roll of 7.5 degrees tilts the view by 7.5 degrees. Yaw and pitch
  were always right.
- Fixed: a camera roll is no longer kept in the view of a sleeping player (NeoForge sets it back to 0 there).

## Version 1.0.4

- Faster: chunk building with Sodium. For every block, face and model Sporran asks whether its class overrides a
  NeoForge rendering method (ambient occlusion, quads, model data, render types, light emission, face hiding, fluid
  overlay). Each of these questions created two short-lived objects and searched a map again; in a flight through a
  large modpack they took 14 % of the working time of Sodium's chunk build threads and produced about a quarter of the
  game's garbage. The answer is now looked up directly. The same was done for the vanilla chunk builder, the light
  engine, the lightmap, the default spawn check of blocks and the item, armor, elytra, boat, camera and container
  screen rendering.
- Faster: all other checks of this kind (`SporranHelper.hasMethodOverride`) find their cached answer without creating a
  key object first.
- No change in behaviour from the two points above: every check gives the same answer as before.
- Fixed: in the first world of a game run (and on a dedicated server until the next `/reload`) the trapped chest, chest
  minecart, chest boat and bamboo chest raft recipes had an empty chest slot. Recipe viewers showed them without the
  chest (EMI logged `Exception thrown setting remainders for minecraft:trapped_chest` and ten more), and a server sent
  them to its players that way. These recipes use NeoForge's `neoforge:difference` ingredient ("wooden chests without
  trapped chests"). An ingredient keeps the items it finds the first time it is asked, and a mod that looks at the
  recipes while they are loaded (Sawmill, for example) asks before the item tags exist. Such a mod puts a plain tag
  ingredient right again itself, but it cannot see the two tag ingredients inside the NeoForge one, which then stayed
  empty. Ingredients that were asked before the tags of the first data pack load were bound now look their items up
  again once they are. Ingredients that have items, and all later loads, are untouched.
- Fixed: a server crash (`StackOverflowError: Ticking entity`, entity `minecraft:magma_cube`) when a magma cube swims
  in a fluid other than lava that is not plain water, for example a fluid of a NeoForge mod (Astral Dimension spawns
  magma cubes next to its own fluids). NeoForge's `jumpInFluid` gives a magma cube its own jump in lava and the jump
  of any other mob in every other fluid; Sporran's version of the second case called itself without end. It now does
  that jump once.
- Fixed: a stack trace on every start (`AnalyzerException: Error at instruction 95: Insufficient maximum stack size`)
  when another mod's mixin captures local variables in `LiquidBlock` (BetterNether does). Sporran makes the
  `LiquidBlock` methods that read the block's fluid call `getFluid()` first and left the unused result on the stack,
  one slot more than the method declares, so Mixin's analysis of the method failed. The result is now dropped again;
  the call itself stays.

## Version 1.0.3

- Fixed: joining a dedicated server failed with "Registry remapping failed" (`value already present`) and then crashed
  the client when another Fabric mod keeps its own `Holder.Reference` subclass in a registry, for example Puffish
  Attributes' aliases for its old attribute ids. NeoForge's key-based `Holder.Reference` equality now only applies
  between plain registry references and holders such as `DeferredHolder`; other mods' `Holder.Reference` subclasses
  keep vanilla identity.

## Version 1.0.2

- Fixed: a crash on saving (`StackOverflowError` while saving a player or mob) when a stronger potion effect ran out
  and a weaker one of the same type took over. This is caused by a bug in newer Porting Lib versions bundled by other
  mods (for example Twilight Forest); Sporran now works around it.

## Version 1.0.1

- Fixed: mobs between 0.75 and 1 block wide never walked along their path (they stood still while chasing or
  wandering). This hit many mobs of NeoForge mods (for example Cataclysm's Koboleton and Astral Dimension's Bovelin,
  Astranite Cutter, Corrupted Astral Golem and Gloom) and also vanilla mobs such as cows, pigs and sheep.
  Pathfinding now matches NeoForge.
- Fixed: arrow-type projectiles from NeoForge mods ran their hit logic on every tick of flight. L_Ender's
  Cataclysm's Void Scatter Arrow burst at the shooter, the thrown Ceraunus and Brontes returned immediately, and
  Scylla's Ceraunus throw did not reach its target. Arrows now behave as on NeoForge.
- Fixed: `ProjectileImpactEvent` now fires for arrows and tridents (as on NeoForge), so mods that react to or cancel
  arrow impacts work.

## Renaming

Everything Kilt-named was renamed to Sporran:

- Fabric mod ID `sporran` (Kilt: `kilt`). Mod name, description, icon and version (`1.0.0+mc1.21.1`) are Sporran's own.
  For compatibility the jar still provides the mod ID `kilt` (next to `neoforge`) and keeps one interface under Kilt's
  package name, `xyz.bluspring.kilt.injections.client.renderer.entity.BoatRendererInjection`: Twilight Forest (Fabric)
  enables its Kilt compatibility mixins, which implement that interface, when `kilt` is loaded (and otherwise applies
  mixins that conflict with Sporran), and Stellar View changes its shader ID handling when `kilt` is loaded.
- Java/Kotlin package `dev.sporran` (Kilt: `xyz.bluspring.kilt`), class names `Sporran*` (Kilt: `Kilt*`), mixin member
  prefix `sporran$` (Kilt: `kilt$`), the `sporran:` ID namespace, the Knit loader ID `sporran`, mixin/class tweaker/mapping
  resource names, `-Dsporran.*` system properties (the `-Dkilt.*` ones are no longer read), the nested compat modules
  (`sporran_*`) and all log, error and branding texts.
- The bundled NeoForge and FancyModLoader code (`forge/`, `fml/`) follows the same renames; `FMLLoader` reports the
  launch handler `sporran`, the title screen and server brand say Sporran.
- The nested Fabric-ASM fork is shipped with the mod ID `mm_sporran` (Kilt: `mm_kilt`); its classes are unchanged, and
  the entrypoint key `mm_kilt:early_risers` that its code reads is kept.
- Cache and config: `<game dir>/.sporran/` and `config/sporran_overrides.json`.

## Fixes

### Drops and death events

- Block drops were added to the world twice, which logged "UUID of added entity already exists" on every start
  and made cancelling drop events impossible. Drops are now captured instead of added, as NeoForge does.
- Entity deaths caused by NeoForge mods were invisible to Fabric mods and to Architectury mods such as FTB Quests
  kill tasks (upstream KiltMC/Kilt#695). Death events now reach both sides exactly once, and a cancelled death is
  respected.

### Player and gameplay events

- NeoForge's player tick event fired twice per tick, so anything driven by it, such as charge-up timers, ran at
  double speed.
- Several NeoForge events fired twice, had the wrong target or lost their result: critical hits, right-click
  item and entity interaction, left-click block, explosions, and screen mouse events.
- Item tooltips had no player, which broke Curios slot tooltips.
- The respawn event was given the old player instead of the new one.
- Data attachments were not re-synced to the client on login, respawn or dimension change.
- Container data values above 32767 were truncated, because NeoForge's int sync was never applied.

### World generation

- Structures whose placement checks a skull block entity could not generate. The check never worked in production
  (Mojang names were compared against runtime names).
- A race in structure generation could hang the server (vanilla MC-271899, a `HashMap` cache shared between
  threads). NeoForge fixes this; it is now fixed here too.

### Logging

- Log noise was reduced: fewer warnings from duplicate data-map loading, fewer warnings for optional mixins whose
  target is missing, and other repeated messages.

## Performance

Measured on our test setup (not a general benchmark):

- First start about 32% faster.
- Cached start about 11% faster.
- Server tick time under load about 9% lower.

## Upstream issues that do not apply

- KiltMC/Kilt#459 (Curios items or their keybound abilities breaking after a dimension change) does not reproduce
  on 1.21.1. The items stay in the Curios slot and their abilities keep working after a nether portal trip, a
  teleport and a respawn. This is covered by a regression test in the client self-test.

## Known limitations

- It is experimental. Compatibility varies per mod and per mod version, and needs testing. Many NeoForge mods will
  work, but some will not.
- Updating a mod to a new version may need re-testing.
- Remaining minor NeoForge API gaps (inherited from Kilt), none of them needed by the mods tested so far:
  - keyboard screen Post events
  - sleep and spawn-point events
  - `SweepAttackEvent`
  - `EntityInteractSpecific` for spectators
- Some warnings in the log come from the original mods and happen on NeoForge too:
  - missing translations and sounds
  - missing particle textures
  - unknown recipe categories
  - unknown tags
