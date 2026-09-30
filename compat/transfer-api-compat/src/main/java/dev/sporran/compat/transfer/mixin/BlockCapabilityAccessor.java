package dev.sporran.compat.transfer.mixin;

import java.util.List;
import java.util.Map;

import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.IBlockCapabilityProvider;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.level.block.Block;

@Mixin(BlockCapability.class)
public interface BlockCapabilityAccessor<T, C extends @Nullable Object> {
    @Accessor("providers")
    Map<Block, List<IBlockCapabilityProvider<T, C>>> sporran$getProviders();

    @Mutable
    @Accessor("providers")
    void sporran$setProviders(Map<Block, List<IBlockCapabilityProvider<T, C>>> providers);
}
