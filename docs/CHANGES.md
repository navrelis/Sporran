# Changes in Sporran

Sporran is a modified version of Kilt v21.1.13 (https://github.com/KiltMC/Kilt, LGPL-2.1). As the LGPL requires, this
page records what Sporran changed compared to that release: the fixes to NeoForge API behaviour, the renaming, and
what is still known to be missing.

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
