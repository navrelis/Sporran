import net.fabricmc.loom.task.RemapJarTask
import dev.sporran.gradle.loom.SporranLoomPlugin

apply<SporranLoomPlugin>()

base {
    archivesName.set("Sporran-Transfer-Compats")
}

version = property("mod_version") as String

tasks {
    processResources {
        val properties = mutableMapOf(
            "version" to project.version,
            "loader_version" to project.property("loader_min_version"),
            "fabric_version" to project.property("fabric_version"),
            "minecraft_version" to project.property("minecraft_version"),
            "fabric_kotlin_version" to project.property("fabric_kotlin_version")
        )

        for ((key, value) in properties) {
            inputs.property(key, value)
        }

        filteringCharset = "UTF-8"

        filesMatching("fabric.mod.json") {
            expand(properties)
        }
    }
}

// Add compat layer to nested JARs in base Sporran project.
rootProject.tasks.getByName<RemapJarTask>("remapJar").nestedJars.from(project.tasks.getByName("remapJar"))