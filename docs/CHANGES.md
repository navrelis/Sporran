# Changes in Sporran

Sporran is a modified version of Kilt v21.1.13 (https://github.com/KiltMC/Kilt, LGPL-2.1). As the LGPL requires, this
page records what Sporran changed compared to that release: the fixes to NeoForge API behaviour, the renaming, and
what is still known to be missing.

Every upstream source file that Sporran modified (Java and Kotlin files in `src/`, `compat/`, `forge/`, `fml/` and
`loader/`) carries a dated notice in its header: "Changed by the Sporran project between YYYY-MM-DD and YYYY-MM-DD",
with the upstream project and licence it is based on. An existing licence header in a file is left as it was; the notice
comes directly after it. The script `tools/licence/add-change-notices.py` (Python 3, run from the repository root)
finds the modified files with git and refreshes these notices; `--check` only reports files whose notice is missing or
out of date.

## Unreleased

More hooks that replaced vanilla code although no NeoForge mod needed them (continuation of 1.0.1):

- Override checks: about 40 places asked "does this class override the NeoForge method" against the vanilla class
  (`Item`, `Block`, `BakedModel`, `Fluid`, `BlockEntity`, `CommandSourceStack`, `DimensionSpecialEffects`, ...) while
  the method is a default of a NeoForge extension interface, so the answer was always yes. They now ask against the
  extension interface (or, where a Sporran mixin adds the method to a vanilla class, against that class). Among them:
  sound types, block friction, fire spread, light emission, explosion resistance, neighbour changes, path finding,
  item damage and stack size, food, shield disabling, bundle contents, lightmap colours. Places where NeoForge's
  default does more than vanilla (supportsEnchantment for enchanted books, canBoatInFluid, attribute tooltips,
  getToolModifiedState with item abilities) were left as they are.
- Checks that could never be true because of a wrong parameter list now find real overrides: `onBlockExploded`,
  `onDestroyedByPlayer` (server and client), `IItemExtension.onDestroyed`, `getLightEmission`, `useAmbientOcclusion`,
  and `getToolModifiedState` for fire charges and flint and steel. Cats and wolves checked `getFoodProperties` on the
  stack class instead of the item class.
- Hoppers: when the container above or below is a plain vanilla container (NeoForge's `InvWrapper`/`SidedInvWrapper`
  over a block entity or entity that is a `Container`), vanilla's `ejectItems`/`suckInItems` run again; NeoForge item
  handlers of mods keep the NeoForge path.
- `Entity.isEyeInFluid`, `Biome.getMobSettings`/`getGenerationSettings` and `VibrationSystem.getGameEventFrequency`
  run their vanilla bodies; NeoForge's answer (fluid types, biome modifiers, the vibration frequency data map) is
  applied to the result instead of replacing the method.
- `ItemStack.useOn` on the server: NeoForge returns `CommonHooks.onPlaceItemIntoWorld` there, so the vanilla body
  (and every other mod's mixin into it or into its `Item.useOn` call) never ran. The vanilla body runs again; the
  placement handling of `onPlaceItemIntoWorld` (block snapshots, `PlayerDestroyItemEvent`, the (multi) place event with
  the stack at its pre-use count, restoring the blocks and the count on cancel) is done around its `Item.useOn` call.
  The `ITEM_USED` stat is awarded by the vanilla body (for `SUCCESS`/`CONSUME`, not for a cancelled placement).
  Nested `useOn` calls get their own snapshots and events, and capturing is switched back when `Item.useOn` throws.
  `onPlaceItemIntoWorld` itself is unchanged for NeoForge mods that call it (its second half is shared). Also: a
  cancelled `ITEM_BEFORE_BLOCK` event no longer leaves `onItemUseFirst`'s callback set on the stack.
- `ServerPlayerGameMode.destroyBlock` always makes vanilla's `hasCorrectToolForDrops` call again. The override check
  for `canHarvestBlock` asked against `Block`, but it is a default of `IBlockExtension`, so the call was replaced by
  `state.canHarvestBlock(...)` for every block and other mods' mixins on that call never ran. A block that really
  overrides `canHarvestBlock` still decides. For every other block NeoForge's default did exactly one thing, post
  `PlayerEvent.HarvestCheck` with the vanilla answer, and that is now done on the result of the vanilla call (the event
  still fires once per break, with the same input and effect).
- Container data values (`ServerPlayer`'s container synchronizer, `broadcastDataValue`): the vanilla method body runs
  again and only its one `send` is replaced by the `AdvancedContainerSetDataPayload` for clients that have the channel
  (it used to cancel the method at its head, so other mods' injections into it never ran for Sporran clients).
- `Ingredient.neoforge$getCustomIngredient`: the override check was right (`getCustomIngredient` is merged into
  `Ingredient` itself, a soft implementation of `IngredientInjection`, so it is false for every plain ingredient and only
  a subclass that overrides it is asked). But the merged `getCustomIngredient` called `neoforge$getCustomIngredient`,
  so a subclass override that calls `super.getCustomIngredient()` recursed until a stack overflow. It now returns the
  field, like NeoForge's method.

The server self-test phase `vanillaHooks` also checks hoppers, `isAir`, `isEyeInFluid`, the vibration frequency and
block placement through `ItemStack.useOn` (vanilla body, `EntityPlaceEvent` accept/cancel, nested use, exception),
`ServerPlayerGameMode.destroyBlock` (a probe on the `hasCorrectToolForDrops` call, one `HarvestCheck` per break, the
listener's answer decides the drops) and `neoforge$getCustomIngredient` of vanilla and custom ingredients.

## Sporran 1.0.1 (2026-10-27)

Three hooks replaced vanilla code for every item, also for plain vanilla items that no NeoForge mod overrides or
listens to. Fabric mods that hook the same vanilla methods with their own mixins then never ran. Found with the
GameTests of Enchanter's Toolkit in a large modpack. Each hook now takes over only when a NeoForge mod needs it:

- `EnchantmentHelper.getItemEnchantmentLevel`: the check "does this item override `getEnchantmentLevel`" asked
  against `Item`, but the method is a default of `IItemExtension`, so the answer was yes for every item and the vanilla
  method body was skipped. It now asks against `IItemExtension`, like the other override checks. NeoForge's
  `GetEnchantmentLevelEvent` is still applied to the vanilla result. The same wrong check was fixed in the other
  enchantment hooks of `EnchantmentHelper` (all enchantments of a stack, enchantment value, primary item filter).
- Enchanting table pool (`EnchantmentHelper.getAvailableEnchantmentResults`): vanilla's `Enchantment.isPrimaryItem`
  decides again unless the item overrides `isPrimaryItemFor` or `supportsEnchantment`. The default of
  `IItemExtension.isPrimaryItemFor` now calls `Enchantment.isPrimaryItem` instead of a copy of its rule, so mixins into
  it apply wherever NeoForge code asks `ItemStack.isPrimaryItemFor`.
- Grindstone (`GrindstoneMenu` result slot, `onTake`): `CommonHooks.onGrindstoneTake` always took over, so taking the
  result always emptied both inputs NeoForge's way and vanilla's code never ran (a Fabric mod that grinds one book off
  a stack lost the whole stack). Sporran now posts `GrindstoneEvent.OnTakeItem` itself and replaces vanilla's code only
  when a listener cancelled the event or changed the XP or an input; otherwise vanilla runs, with the XP amount the
  listeners saw. The event now gets the player (it was passed as `null`).

The same kind of takeover was fixed in four more hooks, whose NeoForge replacement gave the vanilla result anyway:
`BlockStateBase.isAir` (the override check looked for `isAir` on `IBlockExtension`, which does not declare it),
`ShulkerBoxSlot.mayPlace` (same `Item` vs. `IItemExtension` check), `CauldronBlock.receiveStalactiteDrip` (vanilla
water and lava are left to vanilla) and, on the client, `KeyMapping.same` (key mappings without key modifiers).

A new phase of the server self-test (`vanillaHooks`) hooks these three vanilla methods the way another Fabric mod would
and checks that the hooks run and that the NeoForge events still work.

## Sporran 1.0.0 (2026-10-26)

This is the first release of the relaunched Sporran project. It lists everything Sporran changes compared with
Kilt 21.1.13, grouped by theme. The builds that were published before this release are withdrawn, see
[Withdrawn builds](#withdrawn-builds); everything they changed is included here.

### Renaming

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
- `ProjectileImpactEvent` now fires for arrows and tridents (as on NeoForge), so mods that react to or cancel
  arrow impacts work.
- Item tooltips had no player, which broke Curios slot tooltips.
- The respawn event was given the old player instead of the new one.
- Data attachments were not re-synced to the client on login, respawn or dimension change.
- Container data values above 32767 were truncated, because NeoForge's int sync was never applied.

### Mobs, projectiles and fluids

- Fixed: mobs between 0.75 and 1 block wide never walked along their path (they stood still while chasing or
  wandering). This hit many mobs of NeoForge mods (for example Cataclysm's Koboleton and Astral Dimension's Bovelin,
  Astranite Cutter, Corrupted Astral Golem and Gloom) and also vanilla mobs such as cows, pigs and sheep.
  Pathfinding now matches NeoForge.
- Fixed: arrow-type projectiles from NeoForge mods ran their hit logic on every tick of flight. L_Ender's
  Cataclysm's Void Scatter Arrow burst at the shooter, the thrown Ceraunus and Brontes returned immediately, and
  Scylla's Ceraunus throw did not reach its target. Arrows now behave as on NeoForge.
- Fixed: a server crash (`StackOverflowError: Ticking entity`, entity `minecraft:magma_cube`) when a magma cube swims
  in a fluid other than lava that is not plain water, for example a fluid of a NeoForge mod (Astral Dimension spawns
  magma cubes next to its own fluids). NeoForge's `jumpInFluid` gives a magma cube its own jump in lava and the jump
  of any other mob in every other fluid; Sporran's version of the second case called itself without end. It now does
  that jump once.
- Fixed: a crash on saving (`StackOverflowError` while saving a player or mob) when a stronger potion effect ran out
  and a weaker one of the same type took over. This is caused by a bug in newer Porting Lib versions bundled by other
  mods (for example Twilight Forest); Sporran now works around it.

### Camera and rendering

- Fixed: the screen shake of NeoForge mods spun the picture instead of tilting it. A NeoForge mod can tilt the camera
  (the "roll") through `ViewportEvent.ComputeCameraAngles` and `Camera#setRotation(yaw, pitch, roll)`; it gives the
  roll in degrees, like yaw and pitch. Sporran handed that number on in degrees where the game expects radians (and
  with the sign the other way round): the roll was 57 times too strong. A boss slam that is meant to tilt the screen by
  a few degrees turned it by a full circle and more, many times a second, so that nothing could be seen. The roll is now
  converted as in NeoForge (`-roll * (PI / 180)`), so a roll of 7.5 degrees tilts the view by 7.5 degrees. Yaw and pitch
  were always right.
- Fixed: a camera roll is no longer kept in the view of a sleeping player (NeoForge sets it back to 0 there).
- Fixed: `IncompatibleClassChangeError: Conflicting default methods` on `getAppearance`. NeoForge's `IBlockExtension`
  declares `getAppearance(BlockState, BlockAndTintGetter, BlockPos, Direction, BlockState, BlockPos)` as a default
  method, and Sporran's `IBlockExtension` extends Fabric API's `FabricBlock`, which has a default method with the same
  name and descriptor. A block class that also gets `FabricBlock` through a third interface that overrides the method
  (FramedBlocks' `IFramedBlock`) therefore inherited two unrelated most-specific defaults, and the JVM threw on every
  call, for example when connected textures asked a framed block for its appearance. `IBlockExtension` no longer
  re-declares it: the method is inherited from `FabricBlock`, whose default also returns the given state, so NeoForge
  mods that call or override it (same name and descriptor) behave as before.
- Fixed: a stack trace on every start (`AnalyzerException: Error at instruction 95: Insufficient maximum stack size`)
  when another mod's mixin captures local variables in `LiquidBlock` (BetterNether does). Sporran makes the
  `LiquidBlock` methods that read the block's fluid call `getFluid()` first and left the unused result on the stack,
  one slot more than the method declares, so Mixin's analysis of the method failed. The result is now dropped again;
  the call itself stays.

### Recipes, registries and networking

- Fixed: in the first world of a game run (and on a dedicated server until the next `/reload`) the trapped chest, chest
  minecart, chest boat and bamboo chest raft recipes had an empty chest slot. Recipe viewers showed them without the
  chest (EMI logged `Exception thrown setting remainders for minecraft:trapped_chest` and ten more), and a server sent
  them to its players that way. These recipes use NeoForge's `neoforge:difference` ingredient ("wooden chests without
  trapped chests"). An ingredient keeps the items it finds the first time it is asked, and a mod that looks at the
  recipes while they are loaded (Sawmill, for example) asks before the item tags exist. Such a mod puts a plain tag
  ingredient right again itself, but it cannot see the two tag ingredients inside the NeoForge one, which then stayed
  empty. Ingredients that were asked before the tags of the first data pack load were bound now look their items up
  again once they are. Ingredients that have items, and all later loads, are untouched.
- Fixed: joining a dedicated server failed with "Registry remapping failed" (`value already present`) and then crashed
  the client when another Fabric mod keeps its own `Holder.Reference` subclass in a registry, for example Puffish
  Attributes' aliases for its old attribute ids. NeoForge's key-based `Holder.Reference` equality now only applies
  between plain registry references and holders such as `DeferredHolder`; other mods' `Holder.Reference` subclasses
  keep vanilla identity.

### World generation and data pack overlays

- Structures whose placement checks a skull block entity could not generate. The check never worked in production
  (Mojang names were compared against runtime names).
- A race in structure generation could hang the server (vanilla MC-271899, a `HashMap` cache shared between
  threads). NeoForge fixes this; it is now fixed here too.
- Fixed: Fabric mods that declare both `fabric:overlays` and `neoforge:overlays` in `pack.mcmeta` (for example
  Lithostitched 1.8.1) no longer get their NeoForge overlays applied on top. Fabric API already decides the overlays of
  such a pack with the conditions the author wrote for Fabric. Sporran additionally read `neoforge:overlays` for every
  pack in `Pack.readPackMetadata` and decoded the entry conditions through `ConditionalOps`, where a condition that is
  not a NeoForge one (here `apollib:config`) falls back to "true". Overlays the author had switched off by config were
  applied anyway: Lithostitched's `overlay.debug_suite` added armadillos with weight 200 to every Overworld biome and
  sky islands. `PackInject` now skips the `neoforge:overlays` directories of a pack that also has a `fabric:overlays`
  section (logged at DEBUG with the pack id). Packs without `fabric:overlays`, such as NeoForge mods, behave as before,
  and the fallback in `ICondition.CODEC` is unchanged. Chunks generated before the fix keep what was generated.
- Changed: Terralith 2.6.2 (`Terralith_1.21.x_v2.6.2.jar`) now follows `config/terralith.json`. It declares six
  overlays in both `fabric:overlays` and `neoforge:overlays`, gated by `terralith:config` (`custom_structures`,
  `intro_message`, `skylands`, `terrain_slabs` with `invert: true` for the `disable.*` overlays; `recipe_changes`,
  `vanilla_stone_gen` for the `enable.*` overlays). Before this fix the unknown condition fell back to "true", so all
  six were always applied: custom structures, skylands and terrain slabs off, recipe changes and vanilla stone
  generation on, intro message off, whatever the config said. With Terralith's default config, new chunks now get its
  structures, skylands biomes, terrain slabs and stone generation (seams against older chunks), and its recipe changes
  are gone. To keep the previous behaviour, set under `"modules"`: `custom_structures`, `skylands`, `terrain_slabs`,
  `intro_message` false; `recipe_changes`, `vanilla_stone_gen` true.

### Other mods

- Fixed: the spark profiler's mod-sources view. spark builds a table from every Fabric mod's root path to the mod id and
  stops on a path that two mods share. Sporran adds the container `neoforge`, whose root path is the Sporran jar, the
  same as Sporran's own, so spark logged "Multiple entries with same key" and its profiles had no per-mod "Sources"
  view. A compat mixin now leaves the repeated root path out of that table, so the first mod keeps it. Sporran's own
  containers are not changed.

### Logging

- Log noise was reduced: fewer warnings from duplicate data-map loading, fewer warnings for optional mixins whose
  target is missing, and other repeated messages.

### Performance

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

## Withdrawn builds

Builds 1.0.0 to 1.0.8 were published between 2026-09-30 and 2026-10-26 and have been withdrawn. Their exact source is
in the git tags `legacy-1.0.0` to `legacy-1.0.8`. All of their changes are included in the Sporran 1.0.0 section above.
