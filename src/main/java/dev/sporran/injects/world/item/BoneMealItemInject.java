package dev.sporran.injects.world.item;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.sugar.Local;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import dev.sporran.helpers.mixin.CreateStatic;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(BoneMealItem.class)
public abstract class BoneMealItemInject extends Item {
    @Shadow
    public static boolean growCrop(ItemStack stack, Level level, BlockPos pos) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Unique private static final ThreadLocal<Player> sporran$playerRef = new ThreadLocal<>();

    public BoneMealItemInject(Properties properties) {
        super(properties);
    }

    @Definition(id = "blockState", local = @Local(type = BlockState.class))
    @Definition(id = "level", local = @Local(type = Level.class, argsOnly = true))
    @Definition(id = "getBlockState", method = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;")
    @Expression("blockState = level.getBlockState(?)")
    @Inject(method = "growCrop", at = @At(value = "MIXINEXTRAS:EXPRESSION", shift = At.Shift.AFTER), cancellable = true)
    private static void sporran$fireBoneMealEvent(ItemStack stack, Level level, BlockPos pos, CallbackInfoReturnable<Boolean> cir, @Local BlockState state) {
        var event = EventHooks.fireBonemealEvent(sporran$playerRef.get(), level, pos, state, stack);
        if (event.isCanceled())
            cir.setReturnValue(event.isSuccessful());
    }

    @CreateStatic
    private static boolean applyBonemeal(ItemStack stack, Level level, BlockPos pos, @Nullable Player player) {
        sporran$playerRef.set(player);
        boolean value = growCrop(stack, level, pos);
        sporran$playerRef.remove();
        return value;
    }
}
