/*
 * Changed by the Sporran project between 2026-09-29 and 2026-10-27; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.inventory;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.GrindstoneEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import dev.sporran.injections.world.inventory.GrindstoneMenuInjection;
import dev.sporran.mixin.world.inventory.GrindstoneMenuAccessor;

@Mixin(GrindstoneMenu.class)
public abstract class GrindstoneMenuInject extends AbstractContainerMenu implements GrindstoneMenuInjection {
    @Shadow @Final private Container resultSlots;
    @Shadow @Final private Container repairSlots;
    @Unique private int xp = -1;

    protected GrindstoneMenuInject(@Nullable MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    @Override
    public int sporran$getXp() {
        return xp;
    }

    @Mixin(targets = "net.minecraft.world.inventory.GrindstoneMenu$2")
    public abstract static class AnonymousRepairSlot0Inject {
        @ModifyReturnValue(method = "mayPlace", at = @At("RETURN"))
        private boolean sporran$checkCanGrindstoneRepair(boolean original, ItemStack stack) {
            return original || stack.canGrindstoneRepair();
        }
    }

    @Mixin(targets = "net.minecraft.world.inventory.GrindstoneMenu$3")
    public abstract static class AnonymousRepairSlot1Inject {
        @ModifyReturnValue(method = "mayPlace", at = @At("RETURN"))
        private boolean sporran$checkCanGrindstoneRepair(boolean original, ItemStack stack) {
            return original || stack.canGrindstoneRepair();
        }
    }

    @Mixin(targets = "net.minecraft.world.inventory.GrindstoneMenu$4")
    public abstract static class AnonymousResultSlotInject {
        @Shadow @Final private GrindstoneMenu field_16780;

        @Shadow protected abstract int getExperienceAmount(Level level);

        /** Sporran: the XP the OnTakeItem listeners saw, for the vanilla body of the current onTake call; -1 if none. */
        @Unique private int sporran$takeXp = -1;

        // Sporran: this used to call CommonHooks.onGrindstoneTake, which always returns true, so vanilla's onTake (and
        //  every other mod's mixin into it) never ran, also when no NeoForge mod listens to the event. Now the event is
        //  posted here, and the NeoForge result replaces vanilla's only when a listener cancelled the event or changed
        //  what it would do. Otherwise vanilla's body runs, which does exactly what NeoForge's default does (award the
        //  XP, play the sound, empty both inputs), with the XP amount the listeners saw.
        @Inject(method = "onTake", at = @At("HEAD"), cancellable = true)
        private void sporran$checkGrindstoneEvent(Player player, ItemStack stack, CallbackInfo ci) {
            Container inputSlots = ((GrindstoneMenuAccessor) field_16780).getRepairSlots();
            ContainerLevelAccess access = ((GrindstoneMenuAccessor) field_16780).getAccess();
            this.sporran$takeXp = -1;

            boolean takeOver = access.evaluate((level, pos) -> {
                int xp = this.getExperienceAmount(level);
                GrindstoneEvent.OnTakeItem event = new GrindstoneEvent.OnTakeItem(access, player, inputSlots.getItem(0), inputSlots.getItem(1), xp);

                if (NeoForge.EVENT_BUS.post(event).isCanceled()) {
                    return true;
                }

                if (event.getXp() == xp && event.getNewTopItem().isEmpty() && event.getNewBottomItem().isEmpty()) {
                    this.sporran$takeXp = xp;
                    return false;
                }

                // The rest of CommonHooks.onGrindstoneTake.
                if (level instanceof ServerLevel serverLevel) {
                    ExperienceOrb.award(serverLevel, Vec3.atCenterOf(pos), event.getXp());
                }
                level.levelEvent(1042, pos, 0);
                inputSlots.setItem(0, event.getNewTopItem());
                inputSlots.setItem(1, event.getNewBottomItem());
                inputSlots.setChanged();
                return true;
            }, false);

            if (takeOver)
                ci.cancel();
        }

        @Inject(method = "onTake", at = @At("TAIL"))
        private void sporran$clearTakeXp(Player player, ItemStack stack, CallbackInfo ci) {
            this.sporran$takeXp = -1;
        }

        @Inject(method = "getExperienceAmount", at = @At("HEAD"), cancellable = true)
        private void sporran$useStoredXp(Level level, CallbackInfoReturnable<Integer> cir) {
            if (this.sporran$takeXp > -1)
                cir.setReturnValue(this.sporran$takeXp);
            else if (((GrindstoneMenuInjection) field_16780).sporran$getXp() > -1)
                cir.setReturnValue(((GrindstoneMenuInjection) field_16780).sporran$getXp());
        }
    }

    @WrapWithCondition(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Container;setItem(ILnet/minecraft/world/item/ItemStack;)V", ordinal = 0))
    private boolean sporran$setGrindstoneChangeXp(Container instance, int i, ItemStack stack) {
        this.xp = CommonHooks.onGrindstoneChange(this.repairSlots.getItem(0), this.repairSlots.getItem(1), this.resultSlots, -1);

        return this.xp == Integer.MIN_VALUE;
    }

    @WrapOperation(method = "mergeItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isDamageableItem()Z"))
    private boolean sporran$checkIsRepairable(ItemStack instance, Operation<Boolean> original, @Local(ordinal = 0) LocalIntRef i) {
        if (!instance.isRepairable())
            i.set(instance.getDamageValue());

        return original.call(instance) && instance.isRepairable();
    }

    @Inject(method = "mergeItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;setDamageValue(I)V", shift = At.Shift.AFTER))
    private void sporran$setDamageValueIfIsNotRepairable(ItemStack inputItem, ItemStack additionalItem, CallbackInfoReturnable<ItemStack> cir, @Local(ordinal = 2) ItemStack stack) {
        if (!inputItem.isRepairable()) // TODO: i might have the params wrong.
            stack.setDamageValue(additionalItem.getDamageValue());
    }
}
