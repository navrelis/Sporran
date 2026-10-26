# Sporran

A compatibility layer that loads NeoForge 1.21.1 mods on Fabric.

Sporran is a Fabric mod that bundles the NeoForge API and loads NeoForge mods on Fabric Loader. Licence information and credits
are at the end of this page; [docs/CHANGES.md](docs/CHANGES.md) lists what was changed.

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
- [Credits](#credits)

## What you need

Everything in this table is required on **both the client and the server**, except where noted.

| Component | Version | Notes |
| --- | --- | --- |
| Minecraft | 1.21.1 | |
| Java | 21 | |
| [Fabric Loader](https://fabricmc.net/use/) | 0.19.5 or newer | `fabric.mod.json` only enforces 0.19.3, deliberately, but use 0.19.5 or newer. |
| [Fabric API](https://modrinth.com/mod/fabric-api) | 0.116.17+1.21.1 (tested) | |
| [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin) | 1.14.1+kotlin.2.4.20 (tested) | This version needs Fabric Loader 0.19.5 or newer. |
| Sporran (`Sporran-<version>.jar`) | latest | Download it from the [Sporran page on CurseForge](https://www.curseforge.com/minecraft/mc-mods/sporran-neoforge-to-fabric-translator). The source of every release is in this repository ([tags](https://github.com/navrelis/Sporran/tags)). |

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

- Kilt - provides `neoforge` and the same NeoForge classes; install only one of them.
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
**Do not report problems to the authors of the mods you run through Sporran or of the projects Sporran is based on.**
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
- It is a modified version of an LGPL-2.1 project (see [NOTICE](NOTICE)). The modifications are listed in [docs/CHANGES.md](docs/CHANGES.md).
- It includes [NeoForge](https://github.com/neoforged/NeoForge) code (LGPL-2.1) and [Porting Lib](https://github.com/Fabricators-of-Create/Porting-Lib), together with the other libraries listed in `build.gradle.kts`, under their respective licenses.
- **No third-party mods are included in or redistributed by this project.** NeoForge mods must be obtained from their authors, and each mod's licence and its author's wishes must be respected.
- Sporran is an independent compatibility layer. It is **not affiliated with, endorsed by, or supported by any mod author**, nor by Mojang, Microsoft, NeoForged, FabricMC or the authors of the projects Sporran is based on.

## Credits

Sporran is licensed under the LGPL-2.1 ([LICENSE](LICENSE)). It is based on [Kilt](https://github.com/KiltMC/Kilt) and
bundles NeoForge, FancyModLoader, [Porting Lib](https://github.com/Fabricators-of-Create/Porting-Lib) and other
libraries under their own licences; the full notices are in [NOTICE](NOTICE).

For compatibility with Fabric mods that check for Kilt (Twilight Forest, Stellar View), Sporran also provides the mod ID
`kilt` and keeps one interface under Kilt's package name; see [docs/CHANGES.md](docs/CHANGES.md).
