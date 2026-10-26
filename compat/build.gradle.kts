/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

subprojects {
    apply(plugin = "fabric-loom")

    dependencies {
        implementation(project(rootProject.path, configuration = "namedElements"))

        api("net.neoforged:bus:${property("eventbus_version")}") {
            exclude("org.ow2.asm")
        }
    }

    // Each module's fabric.mod.json declares "icon": "icon.png"; ship Sporran's icon at that path so Mod Menu shows it.
    tasks.named<ProcessResources>("processResources") {
        from(rootProject.file("src/main/resources/assets/sporran/icon.png"))
    }
}