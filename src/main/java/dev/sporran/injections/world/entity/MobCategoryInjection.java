/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.entity;

import net.minecraft.world.entity.MobCategory;

import java.util.HashMap;
import java.util.Map;

public interface MobCategoryInjection {
    Map<String, MobCategory> BY_NAME = new HashMap<>();

    static MobCategory byName(String name) {
        return BY_NAME.get(name);
    }
}
