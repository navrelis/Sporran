package xyz.bluspring.kilt.injections.client.renderer.entity;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.model.ListModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;

/**
 * Compatibility alias under the upstream package name, kept on purpose. Twilight Forest (Fabric) ships mixins for the
 * upstream project that implement this exact interface name ({@code twilightforest.mixin.compat.KiltBoatRendererMixin}
 * and {@code KiltBoatRendererCompatibilityMixin}, enabled when the mod ID {@code kilt} is loaded, which Sporran
 * provides). The method is the same as {@link dev.sporran.injections.client.renderer.entity.BoatRendererInjection}'s,
 * so their implementation also serves Sporran's interface.
 */
public interface BoatRendererInjection extends dev.sporran.injections.client.renderer.entity.BoatRendererInjection {
    @Override
    Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(Boat boat);
}
