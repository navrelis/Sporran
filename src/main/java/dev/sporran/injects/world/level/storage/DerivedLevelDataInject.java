/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.level.storage;

import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DerivedLevelData.class)
public abstract class DerivedLevelDataInject implements ServerLevelData {
    @Shadow
    @Final
    private ServerLevelData wrapped;

    @Override
    public void setDayTimeFraction(float dayTimeFraction) {
        this.wrapped.setDayTimeFraction(dayTimeFraction);
    }

    @Override
    public float getDayTimeFraction() {
        return this.wrapped.getDayTimeFraction();
    }

    @Override
    public float getDayTimePerTick() {
        return this.wrapped.getDayTimePerTick();
    }

    @Override
    public void setDayTimePerTick(float dayTimePerTick) {
        this.wrapped.setDayTimePerTick(dayTimePerTick);
    }
}
