/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package cpw.mods.modlauncher.api

// This isn't actually to be used by anything related to Sporran,
// but this is implemented for the sake of mod compatibility
interface INameMappingService {
    enum class Domain {
        CLASS, METHOD, FIELD
    }
}