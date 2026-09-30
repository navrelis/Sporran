package dev.sporran.injections.world.level.block.state;

import java.util.function.Supplier;

import dev.sporran.util.SporranHelper;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

public interface BlockBehaviourInjection {
    default boolean isAir(BlockState state) {
        throw SporranHelper.createMixinException(BlockBehaviourInjection.class, "isAir");
    }

    interface PropertiesInjection {
        default Supplier<ResourceKey<LootTable>> getLootTableSupplier() {
            throw new IllegalStateException();
        }
        default BlockBehaviour.Properties lootFrom(Supplier<? extends Block> blockIn) {
            throw new IllegalStateException();
        }
    }

    interface BlockStateBaseInjection {
        default boolean sporran$isAir() {
            throw SporranHelper.createMixinException(BlockStateBaseInjection.class, "sporran$isAir");
        }
    }
}
