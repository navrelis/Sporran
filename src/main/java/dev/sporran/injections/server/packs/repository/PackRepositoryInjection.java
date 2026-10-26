/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.server.packs.repository;

import net.minecraft.server.packs.repository.RepositorySource;

public interface PackRepositoryInjection {
    default void addPackFinder(RepositorySource packFinder) {
        throw new IllegalStateException();
    }
}
