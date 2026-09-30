package dev.sporran.compat.fabric.mixin.cctweaked;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import dev.sporran.compat.fabric.cctweaked.ComponentAccessImplExt;

import java.util.function.Consumer;

@Mixin(targets = "dan200.computercraft.shared.platform.PlatformHelperImpl$ComponentAccessImpl")
public abstract class ComponentAccessImplMixin implements ComponentAccessImplExt {
    @Shadow
    protected abstract ServerLevel getLevel();

    @Shadow
    @Final
    private BlockEntity owner;
    @Unique
    private BlockCapability<?, Direction> sporran$capability;
    @Unique
    private Consumer<Direction> sporran$invalidate;
    @Unique
    BlockCapabilityCache<?, Direction>[] sporran$caches = new BlockCapabilityCache[6];

    @Unique
    @Override
    public void sporran$initializeCapabilityLookups(BlockCapability<?, Direction> capability, Consumer<Direction> invalidate) {
        sporran$capability = capability;
        sporran$invalidate = invalidate;
    }

    @ModifyReturnValue(method = "get", at = @At("RETURN"))
    public Object sporran$tryCapabilityLookup(@Nullable Object original, @Local(argsOnly = true, name = "direction") Direction direction) {
        if (original != null || sporran$capability == null)
            return original;

        var level = getLevel();
        var cache = sporran$caches[direction.ordinal()];
        if (cache == null) {
            cache = sporran$caches[direction.ordinal()] = BlockCapabilityCache.create(
                sporran$capability, level, owner.getBlockPos().relative(direction),
                direction.getOpposite(), () -> !owner.isRemoved(), () -> sporran$invalidate.accept(direction)
            );
        }

        return cache.getCapability();
    }
}
