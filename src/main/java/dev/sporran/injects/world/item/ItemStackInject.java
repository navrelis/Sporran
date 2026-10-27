// TRACKED HASH: debf6874a4415fcbb527c106a281c5bd27a0b454
/*
 * Changed by the Sporran project between 2026-09-29 and 2026-10-27; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import net.neoforged.neoforge.common.extensions.IItemStackExtension;
import net.neoforged.neoforge.common.util.BlockSnapshot;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import dev.sporran.injections.world.item.ItemStackInjection;
import dev.sporran.util.SporranHelper;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

@Mixin(ItemStack.class)
public abstract class ItemStackInject implements MutableDataComponentHolder, IItemStackExtension, ItemStackInjection {

    @Shadow public abstract Item getItem();
    @Shadow public abstract InteractionResult useOn(UseOnContext context);
    @Shadow public abstract boolean isEmpty();
    @Shadow @Final private PatchedDataComponentMap components;

    @Shadow
    public abstract ItemEnchantments getEnchantments();

    @Shadow
    public abstract boolean is(Item item);

    @Shadow
    public abstract Rarity getRarity();

    @Shadow
    public abstract void hurtAndBreak(int damage, ServerLevel level, @Nullable ServerPlayer player, Consumer<Item> onBreak);

    public ItemStackInject(ItemLike item, int count) {}

    @Override
    public boolean isComponentsPatchEmpty() {
        return !this.isEmpty() ? this.components.isPatchEmpty() : true;
    }

    @ModifyReturnValue(method = "is(Lnet/minecraft/core/Holder;)Z", at = @At("RETURN"))
    private boolean sporran$checkMatchesDeferredHolder(boolean original, @Local(argsOnly = true) Holder<Item> holder) {
        return original || this.is(holder.value());
    }

    @Unique
    private Function<UseOnContext, InteractionResult> sporran$callback;

    // Sporran: NeoForge returns CommonHooks.onPlaceItemIntoWorld here on the server, which skips the vanilla body (and every other mod's
    //  mixin into it). The vanilla body runs instead; the placement handling of onPlaceItemIntoWorld is done around its Item#useOn call
    //  (sporran$tryUseOnCallback / sporran$placeItemIntoWorld).
    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void sporran$tryPlaceItemInWorld(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (this.sporran$callback == null) {
            var e = NeoForge.EVENT_BUS.post(new UseItemOnBlockEvent(context, UseItemOnBlockEvent.UsePhase.ITEM_AFTER_BLOCK));
            if (e.isCanceled()) {
                cir.setReturnValue(e.getCancellationResult().result());
            }
        }
    }

    @Override
    public InteractionResult onItemUseFirst(UseOnContext context) {
        var e = NeoForge.EVENT_BUS.post(new UseItemOnBlockEvent(context, UseItemOnBlockEvent.UsePhase.ITEM_BEFORE_BLOCK));
        if (e.isCanceled()) return e.getCancellationResult().result();
        // Sporran: set after the event and always cleared again (a cancelled event or an exception used to leave it set, so the
        //  next useOn of this stack skipped ITEM_AFTER_BLOCK and the placement and called onItemUseFirst instead of Item#useOn).
        this.sporran$callback = c -> this.getItem().onItemUseFirst((ItemStack) (Object) this, c);
        try {
            return this.useOn(context);
        } finally {
            this.sporran$callback = null;
        }
    }

    @WrapOperation(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;useOn(Lnet/minecraft/world/item/context/UseOnContext;)Lnet/minecraft/world/InteractionResult;"))
    private InteractionResult sporran$tryUseOnCallback(Item instance, UseOnContext context, Operation<InteractionResult> original) {
        if (sporran$callback != null) {
            // Sporran: cleared before the call, so a useOn of this stack from inside onItemUseFirst takes the normal path.
            var callback = sporran$callback;
            sporran$callback = null;

            return callback.apply(context);
        }

        if (context.getLevel().isClientSide()) {
            return original.call(instance, context);
        }

        return sporran$placeItemIntoWorld(instance, context, original);
    }

    /**
     * Sporran: {@link CommonHooks#onPlaceItemIntoWorld} around the {@code Item#useOn} call of the vanilla body (logical server only):
     * block snapshots are captured during the call (not for buckets), {@code PlayerDestroyItemEvent} fires if the stack was used up,
     * and for a result that consumes the action the (multi) place event decides; when it is cancelled the blocks are restored, the stack
     * keeps its pre-use count and the result is FAIL, so the vanilla body awards no stat. Otherwise the vanilla body awards it as usual.
     * <p>
     * All per-call state is local, so a nested {@code useOn} (an item whose {@code useOn} uses another stack) gets its own: it only takes
     * the snapshots captured after it started, leaves the outer call's to it and switches capturing back to what it found. Capturing is
     * switched back also when {@code Item#useOn} or a listener throws.
     */
    @Unique
    private InteractionResult sporran$placeItemIntoWorld(Item item, UseOnContext context, Operation<InteractionResult> original) {
        ItemStack itemstack = context.getItemInHand();
        Level level = context.getLevel();
        Player player = context.getPlayer();

        int size = itemstack.getCount();
        DataComponentMap components = itemstack.getComponents();
        ItemStack copy = itemstack.copy();

        boolean capture = !(item instanceof BucketItem);
        boolean wasCapturing = level.sporran$getCapturingBlockSnapshots();
        ArrayList<BlockSnapshot> captured = level.sporran$getCapturedBlockSnapshots();
        // Not nested: everything in the list is ours, like onPlaceItemIntoWorld. Nested in another capture: only what is added from here on.
        int start = wasCapturing ? captured.size() : 0;

        InteractionResult ret;
        List<BlockSnapshot> blockSnapshots = List.of();
        try {
            if (capture) level.sporran$setCapturingBlockSnapshots(true);
            try {
                ret = original.call(item, context);
                if (itemstack.isEmpty())
                    EventHooks.onPlayerDestroyItem(player, copy, context.getHand());
            } finally {
                if (capture) {
                    // Off while the events fire and the blocks are restored or updated, as in onPlaceItemIntoWorld.
                    level.sporran$setCapturingBlockSnapshots(false);
                    // Taken off the level also on an exception, so they are never left for the next capture.
                    List<BlockSnapshot> own = captured.subList(Math.min(start, captured.size()), captured.size());
                    blockSnapshots = new ArrayList<>(own);
                    own.clear();
                }
            }

            if (ret.consumesAction()) {
                ret = CommonHooks.sporran$firePlaceEvents(context, itemstack, item, size, components, blockSnapshots, ret, false);
            }
        } finally {
            if (capture) level.sporran$setCapturingBlockSnapshots(wasCapturing);
        }

        return ret;
    }

    // Sporran: Ignore wrapEncodingExceptions

    // Sporran: the item hooks below ask IItemExtension, which declares getMaxStackSize, isDamaged, getDamage, setDamage, getMaxDamage and
    //  getDefaultAttributeModifiers as defaults (their bodies are vanilla's); against Item.class every one of the checks was true for every item.

    @WrapOperation(method = "getMaxStackSize", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getOrDefault(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;"))
    private Object tryUseForgeMaxStackSize(ItemStack instance, DataComponentType<Integer> type, Object fallback, Operation<Object> original) {
        if (SporranHelper.INSTANCE.hasMethodOverride(instance.getItem().getClass(), IItemExtension.class, "getMaxStackSize", ItemStack.class)) {
            return instance.getItem().getMaxStackSize((ItemStack) (Object) this);
        }

        return original.call(instance, type, fallback);
    }

    @Definition(id = "getDamageValue", method = "Lnet/minecraft/world/item/ItemStack;getDamageValue()I")
    @Expression("this.getDamageValue() > 0")
    @ModifyExpressionValue(method = "isDamaged", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean tryUseIsDamaged(boolean original) {
        if (SporranHelper.INSTANCE.hasMethodOverride(getItem().getClass(), IItemExtension.class, "isDamaged", ItemStack.class)) {
            return getItem().isDamaged((ItemStack) (Object) this);
        }
        return original;
    }

    @ModifyReturnValue(method = "getDamageValue", at = @At("RETURN"))
    private int tryUseItemGetDamage(int original) {
        if (SporranHelper.INSTANCE.hasMethodOverride(getItem().getClass(), IItemExtension.class, "getDamage", ItemStack.class)) {
            return getItem().getDamage((ItemStack) (Object) this);
        }
        return original;
    }

    @WrapOperation(method = "setDamageValue", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;set(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;"))
    private <T> Object trySetItemDamage(ItemStack instance, DataComponentType<? super T> component, T value, Operation<Object> original) {
        if (SporranHelper.INSTANCE.hasMethodOverride(getItem().getClass(), IItemExtension.class, "setDamage", ItemStack.class, int.class)) {
            getItem().setDamage((ItemStack) (Object) this, (Integer) value);
            return null;
        }

        return original.call(instance, component, value);
    }

    @ModifyReturnValue(method = "getMaxDamage", at = @At("RETURN"))
    private int tryItemGetMaxDamage(int original) {
        if (SporranHelper.INSTANCE.hasMethodOverride(getItem().getClass(), IItemExtension.class, "getMaxDamage", ItemStack.class)) {
            return getItem().getMaxDamage((ItemStack) (Object) this);
        }
        return original;
    }

    @Unique private LivingEntity sporran$currentEntity = null;

    @Override
    public void hurtAndBreak(int damage, ServerLevel level, @Nullable LivingEntity entity, Consumer<Item> onBreak) {
        try {
            this.sporran$currentEntity = entity;
            this.hurtAndBreak(damage, level, null, onBreak);
        } finally {
            this.sporran$currentEntity = null;
        }
    }

    @Definition(id = "player", local = @Local(type = ServerPlayer.class, argsOnly = true))
    @Expression("player == null")
    @ModifyVariable(method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/server/level/ServerPlayer;Ljava/util/function/Consumer;)V", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0), argsOnly = true)
    private int sporran$tryHandleDamageItem(int damage, @Local(argsOnly = true) Consumer<Item> onBroken, @Local(argsOnly = true) ServerPlayer player) {
        var entity = this.sporran$currentEntity != null ? this.sporran$currentEntity : player;
        return this.getItem().damageItem((ItemStack) (Object) this, damage, entity, onBroken);
    }

    @Definition(id = "player", local = @Local(type = ServerPlayer.class, argsOnly = true))
    @Expression("player != null")
    @ModifyExpressionValue(
        method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/server/level/ServerPlayer;Ljava/util/function/Consumer;)V",
        at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 1)
    )
    private boolean sporran$tryHandleCurrentEntityAsServerPlayer(boolean original) {
        return original || this.sporran$currentEntity instanceof ServerPlayer;
    }

    @ModifyArg(method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/server/level/ServerPlayer;Ljava/util/function/Consumer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/critereon/ItemDurabilityTrigger;trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/item/ItemStack;I)V"))
    private ServerPlayer sporran$tryUseCurrentEntityServerPlayer(ServerPlayer player) {
        if (this.sporran$currentEntity instanceof ServerPlayer p)
            return p;

        return player;
    }

    @WrapOperation(method = "hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/server/level/ServerPlayer;Ljava/util/function/Consumer;)V"))
    private void sporran$tryUseNeoHurtAndBreak(ItemStack instance, int damage, ServerLevel level, ServerPlayer player, Consumer<Item> onBreak, Operation<Void> original, @Local(argsOnly = true) LivingEntity entity) {
        try {
            if (player == null && entity != null)
                this.sporran$currentEntity = entity;

            original.call(instance, damage, level, player, onBreak);
        } finally {
            this.sporran$currentEntity = null;
        }
    }

    @Definition(id = "withStyle", method = "Lnet/minecraft/network/chat/MutableComponent;withStyle(Lnet/minecraft/ChatFormatting;)Lnet/minecraft/network/chat/MutableComponent;")
    @Definition(id = "getRarity", method = "Lnet/minecraft/world/item/ItemStack;getRarity()Lnet/minecraft/world/item/Rarity;")
    @Definition(id = "color", method = "Lnet/minecraft/world/item/Rarity;color()Lnet/minecraft/ChatFormatting;")
    @Expression("?.withStyle(this.getRarity().color())")
    @WrapOperation(
        method = {"getTooltipLines", "getDisplayName"},
        at = @At(value = "MIXINEXTRAS:EXPRESSION"),
        expect = 2
    )
    private MutableComponent sporran$applyRarityStyleModifier(
        MutableComponent instance, ChatFormatting chatFormatting, Operation<MutableComponent> original
    ) {
        var rarity = this.getRarity();
        if (rarity.sporran$hasCustomStyleModifier()) {
            return instance.withStyle(rarity.getStyleModifier());
        }
        return original.call(instance, chatFormatting);
    }

    // Sporran: fired here instead of through Fabric's ItemTooltipCallback, which has no player argument. Like NeoForge,
    //  only for the regular tooltip (second return; the first one returns the empty list of a hidden tooltip).
    @ModifyReturnValue(method = "getTooltipLines", at = @At(value = "RETURN", ordinal = 1))
    private List<Component> sporran$onItemTooltip(List<Component> original, Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipFlag) {
        EventHooks.onItemTooltip((ItemStack) (Object) this, player, original, tooltipFlag, tooltipContext);
        return original;
    }

    // Sporran: okay seriously what's the point of this?
    @Override
    public ItemEnchantments getTagEnchantments() {
        return this.getEnchantments();
    }

    @ModifyReceiver(method = "forEachModifier(Lnet/minecraft/world/entity/EquipmentSlotGroup;Ljava/util/function/BiConsumer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/component/ItemAttributeModifiers;forEach(Lnet/minecraft/world/entity/EquipmentSlotGroup;Ljava/util/function/BiConsumer;)V", ordinal = 0))
    private ItemAttributeModifiers computeModifiedAttributesGroup(ItemAttributeModifiers instance, EquipmentSlotGroup slotGroup, BiConsumer<Holder<Attribute>, AttributeModifier> action) {
        return CommonHooks.computeModifiedAttributes((ItemStack) (Object) this, instance);
    }

    @WrapOperation(method = "forEachModifier(Lnet/minecraft/world/entity/EquipmentSlotGroup;Ljava/util/function/BiConsumer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;getDefaultAttributeModifiers()Lnet/minecraft/world/item/component/ItemAttributeModifiers;"))
    private ItemAttributeModifiers getDefaultModifiersAndComputeModifiedAttributesGroup(Item instance, Operation<ItemAttributeModifiers> original) {
        if (SporranHelper.INSTANCE.hasMethodOverride(instance.getClass(), IItemExtension.class, "getDefaultAttributeModifiers", ItemStack.class)) {
            return CommonHooks.computeModifiedAttributes((ItemStack) (Object) this, getItem().getDefaultAttributeModifiers((ItemStack) (Object) this));
        }
        return CommonHooks.computeModifiedAttributes((ItemStack) (Object) this, original.call(instance));
    }

    @ModifyReceiver(method = "forEachModifier(Lnet/minecraft/world/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/component/ItemAttributeModifiers;forEach(Lnet/minecraft/world/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V", ordinal = 0))
    private ItemAttributeModifiers computeModifiedAttributes(ItemAttributeModifiers instance, EquipmentSlot equipmentSlot, BiConsumer<Holder<Attribute>, AttributeModifier> action) {
        return CommonHooks.computeModifiedAttributes((ItemStack) (Object) this, instance);
    }

    @WrapOperation(method = "forEachModifier(Lnet/minecraft/world/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;getDefaultAttributeModifiers()Lnet/minecraft/world/item/component/ItemAttributeModifiers;"))
    private ItemAttributeModifiers getDefaultModifiersAndComputeModifiedAttributes(Item instance, Operation<ItemAttributeModifiers> original) {
        if (SporranHelper.INSTANCE.hasMethodOverride(instance.getClass(), IItemExtension.class, "getDefaultAttributeModifiers", ItemStack.class)) {
            return CommonHooks.computeModifiedAttributes((ItemStack) (Object) this, getItem().getDefaultAttributeModifiers((ItemStack) (Object) this));
        }
        return CommonHooks.computeModifiedAttributes((ItemStack) (Object) this, original.call(instance));
    }

    @TargetHandler(mixin = "io.github.fabricators_of_create.porting_lib.tool.mixin.ItemStackMixin", name = "canPerformAction")
    @Inject(method = "@MixinSquared:Handler", at = @At("HEAD"), cancellable = true, order = 1050)
    private void sporran$checkCanPerformActionForge(io.github.fabricators_of_create.porting_lib.tool.ItemAbility toolAction, CallbackInfoReturnable<Boolean> cir) {
        var forgeToolAction = ItemAbility.sporran$getNullable(toolAction.name());
        if (forgeToolAction != null && this.canPerformAction(forgeToolAction)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "canBeHurtBy", at = @At("HEAD"), cancellable = true)
    private void sporran$checkCanBeHurtBy(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if (!this.getItem().canBeHurtBy((ItemStack) (Object) this, damageSource))
            cir.setReturnValue(false);
    }
}
