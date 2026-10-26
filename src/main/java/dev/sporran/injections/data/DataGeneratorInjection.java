/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import dev.sporran.processor.FabricInjectedInterface;
import dev.sporran.util.SporranHelper;

import java.util.Map;

@FabricInjectedInterface(DataGenerator.class)
public interface DataGeneratorInjection {
    default Map<String, DataProvider> getProvidersView() {
        throw new IllegalStateException();
    }
    default PackOutput getPackOutput() {
        throw new IllegalStateException();
    }
    default PackOutput getPackOutput(String path) {
        throw new IllegalStateException();
    }
    default <T extends DataProvider> T addProvider(boolean run, DataProvider.Factory<T> factory) {
        throw new IllegalStateException();
    }
    default <T extends DataProvider> T addProvider(boolean run, T provider) {
        throw new IllegalStateException();
    }
    default void merge(DataGenerator other) {
        throw SporranHelper.createMixinException(DataGeneratorInjection.class, "merge");
    }
}
