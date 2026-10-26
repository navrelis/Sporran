# Sporran

A compatibility layer that loads NeoForge 1.21.1 mods on Fabric.

Sporran is a Fabric mod that bundles the NeoForge API and loads NeoForge mods on Fabric Loader. Credits and licence
information are at the end of this page; [docs/CHANGES.md](docs/CHANGES.md) lists what was changed.

It is experimental and may crash or break worlds. Back up your worlds first.

**You download the NeoForge mods yourself.** This project does not include or redistribute any mods. Some mod authors
do not want their mods used on Fabric; please respect that.

- [What you need](#what-you-need)
- [Installation](#installation)
- [What goes in the mods folder](#what-goes-in-the-mods-folder)
- [Do not install together with](#do-not-install-together-with)
- [First start, cache and configuration](#first-start-cache-and-configuration)
- [Known limitations](#known-limitations)
- [Reporting bugs](#reporting-bugs)
- [For developers](#for-developers)
- [License and legal](#license-and-legal)
- [Credits and acknowledgements](#credits-and-acknowledgements)

## What you need

Everything in this table is required on **both the client and the server**, except where noted.

| Component | Version | Notes |
| --- | --- | --- |
| Minecraft | 1.21.1 | |
| Java | 21 | |
| [Fabric Loader](https://fabricmc.net/use/) | 0.19.5 or newer | `fabric.mod.json` only enforces 0.19.3, deliberately, but use 0.19.5 or newer. |
| [Fabric API](https://modrinth.com/mod/fabric-api) | 0.116.17+1.21.1 (tested) | |
| [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin) | 1.14.1+kotlin.2.4.20 (tested) | This version needs Fabric Loader 0.19.5 or newer. |
| Sporran (`Sporran-<version>.jar`) | latest | From the Releases page of this repository. |

On top of that, you need the NeoForge 1.21.1 jars of the mods you want to play, and the NeoForge jars of any
libraries they depend on. Get them from their authors' official pages (for example their CurseForge or Modrinth
pages), and respect each mod's licence and the wishes of its author.

Mods that use GeckoLib need **GeckoLib for Fabric** at the matching version. The NeoForge GeckoLib jar is not needed.

## Installation

1. Install Fabric Loader 0.19.5 or newer for Minecraft 1.21.1.
2. Put Fabric API, Fabric Language Kotlin and `Sporran-<version>.jar` into your `mods` folder.
3. Put the NeoForge mods you want to play, and the NeoForge libraries they need, into the same `mods` folder.
4. Do the same on the server and on every client that joins it.
5. Start the game. The first start takes longer, see [First start](#first-start-cache-and-configuration).

### What goes in the mods folder

- `fabric-api`
- `fabric-language-kotlin`
- `Sporran-<version>.jar`
- The NeoForge 1.21.1 jars of the mods you want to play, plus the NeoForge libraries they depend on.
- GeckoLib for **Fabric** at the matching version, if any of your mods uses GeckoLib.

The NeoForge GeckoLib jar is **not** needed. Sporran uses the Fabric one. If only the NeoForge GeckoLib is
present, the client refuses to start and shows a loader message asking for the Fabric version.

## Do not install together with

- Kilt (the project Sporran contains code from; see [Credits](#credits-and-acknowledgements)). Both provide `neoforge` and the same
  NeoForge classes; install only one of them.
- **Embeddium**
- **Async**
- **Sinytra Connector**

## First start, cache and configuration

- On the first start Sporran remaps the NeoForge jars and Minecraft. This takes about 30-40 seconds
  longer than a normal start. Later starts use the cache.
- The cache is in `<game dir>/.sporran/`. It is safe to delete and is rebuilt on the next start.
- `config/sporran_overrides.json` is the optional override file (force-disabled mod IDs and dependency
  overrides). Override files and cache folders
  of other NeoForge-on-Fabric loaders are not read.

## Known limitations

- It is experimental. Many NeoForge mods will work, but some will not. Compatibility with any particular mod or
  version is not guaranteed, and updating a mod to a new version may need re-testing.
- Some warnings in the log come from the mods themselves and also happen on NeoForge, for example missing
  translations and sounds, missing textures, unknown recipe categories and unknown tags.
- A few minor NeoForge event gaps remain. The full list is in [docs/CHANGES.md](docs/CHANGES.md).

## Reporting bugs

Report bugs to this repository's issue tracker.
**Do not report problems to the authors of the mods you run through Sporran, or to the authors of the upstream project listed under Credits.**
They do not support running their mods on Fabric through Sporran, and most issues are caused by the compatibility layer rather than by the mods themselves.

Please include your `latest.log` and the versions of every mod in your `mods` folder.

## For developers

### Building

```
git clone <repository URL>
cd <repository folder>
./gradlew remapJar
```

Use JDK 21. The NeoForge, FancyModLoader and Knit Loader sources are vendored in `forge/`, `fml/` and `loader/` (see [VENDORED.md](VENDORED.md)), no submodules are needed.

The jar ends up in `build/libs/` as `Sporran-<version>.jar` (ignore the `-cichlid-libs` jar). Add
`-Pbuild.release=true` to drop the `-local.<commit>` suffix from the version.
Use `remapJar` only. The root `build` and `shadowJar` tasks are not used and are known to fail.

### Testing

The automated test harness (dedicated server, client and modpack self-tests) is kept in the development repository and is not part of the public source release.

## License and legal

- This project is licensed under the **GNU Lesser General Public License v2.1** (LGPL-2.1). See [LICENSE](LICENSE).
- It is a modified version of an LGPL-2.1 project, see [Credits](#credits-and-acknowledgements). The modifications are listed in [docs/CHANGES.md](docs/CHANGES.md).
- It includes [NeoForge](https://github.com/neoforged/NeoForge) code (LGPL-2.1) and [Porting Lib](https://github.com/Fabricators-of-Create/Porting-Lib), together with the other libraries listed in `build.gradle.kts`, under their respective licenses.
- **No third-party mods are included in or redistributed by this project.** NeoForge mods must be obtained from their authors, and each mod's licence and its author's wishes must be respected.
- Sporran is an independent compatibility layer. It is **not affiliated with, endorsed by, or supported by any mod author**, nor by Mojang, Microsoft, NeoForged, FabricMC or the authors of the upstream project listed under Credits.

## Credits and acknowledgements

Sporran contains code from [Kilt](https://github.com/KiltMC/Kilt) (version 21.1.13) by KiltMC, originally written by
BluSpring and AlphaMode with the contributors listed in `fabric.mod.json`. Kilt is licensed under the LGPL-2.1, and so
is Sporran. The bundled NeoForge, FancyModLoader and Knit Loader code comes from the Kilt project's repositories
(see [VENDORED.md](VENDORED.md)).

For compatibility with Fabric mods that check for Kilt (Twilight Forest, Stellar View), Sporran also provides the mod ID
`kilt` and keeps one interface under Kilt's package name; see [docs/CHANGES.md](docs/CHANGES.md).

Sporran also builds on [NeoForge](https://github.com/neoforged/NeoForge) (LGPL-2.1) and
[Porting Lib](https://github.com/Fabricators-of-Create/Porting-Lib) by the Fabricators of Create.

The following is carried over from the original Kilt README.

I want to give a huge amount of thanks to the [Fabricators of Create](https://github.com/Fabricators-of-Create)
for making [Porting Lib](https://github.com/Fabricators-of-Create/Porting-Lib),
as without it, this would have been significantly harder to do.

Thank you to the [Minecraft Forge](https://github.com/MinecraftForge) developers, [cpw](https://github.com/cpw) and [LexManos](https://github.com/LexManos), and all of its contributors,
for making the Forge API, and having it open-sourced.

Thank you to the [FabricMC](https://fabricmc.net) developers, [modmuss50](https://github.com/modmuss50), [sfPlayer1](https://github.com/sfPlayer1), and [asiekierka](https://github.com/asiekierka), for
creating Fabric.

And thank you to my friend [Zuite](https://twitter.com/Zuite_), for being the wall that I
throw all my code frustrations and thought processes at, as
she has helped me tremendously to just stop and think about all of the
problems at hand.

(The credits above are written in the voice of Kilt's original author, BluSpring.)
