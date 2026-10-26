/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.helpers;

/**
 * An annotation to locate which methods have been reimplemented by Porting Lib or related,
 * to add an "implements" on that class.
 */
public @interface FabricImplemented {
    Class<?> value();
}
