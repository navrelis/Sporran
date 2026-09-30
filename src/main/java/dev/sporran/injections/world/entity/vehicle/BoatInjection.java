package dev.sporran.injections.world.entity.vehicle;

import java.util.function.Supplier;

import dev.sporran.util.SporranHelper;

import net.minecraft.world.item.Item;

public interface BoatInjection {
    interface TypeInjection {
        default Item getSticks() {
            throw SporranHelper.createMixinException(TypeInjection.class, "getSticks");
        }

        default boolean isRaft() {
            throw SporranHelper.createMixinException(TypeInjection.class, "isRaft");
        }

        default void sporran$setRaft(boolean raft) {
            throw SporranHelper.createMixinException(TypeInjection.class, "sporran$setRaft");
        }

        default Supplier<Item> sporran$getBoatItem() {
            throw SporranHelper.createMixinException(TypeInjection.class, "sporran$getBoatItem");
        }

        default Supplier<Item> sporran$getChestBoatItem() {
            throw SporranHelper.createMixinException(TypeInjection.class, "sporran$getChestBoatItem");
        }
    }
}
