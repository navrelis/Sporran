/*
 * Changed by the Sporran project between 2026-09-29 and 2026-10-27; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.item.enchantment;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import net.neoforged.neoforge.event.EventHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import dev.sporran.helpers.mixin.CreateStatic;
import dev.sporran.injections.world.item.enchantment.EnchantmentHelperInjection;
import dev.sporran.util.SporranHelper;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperInject implements EnchantmentHelperInjection {
    // Sporran: these checks asked against Item.class, but the methods are defaults of IItemExtension (which Item gets
    //  through interface injection), so they were true for every item and the hooks below replaced vanilla code (and
    //  every other mod's mixin into it) even for plain vanilla items. Asked against IItemExtension, they are only true
    //  for an item class that really overrides the method.
    @Unique private static final SporranHelper.MethodOverrideCheck sporran$GET_ENCHANTMENT_LEVEL_OVERRIDE = SporranHelper.MethodOverrideCheck.of(IItemExtension.class, "getEnchantmentLevel", ItemStack.class, Holder.class);
    @Unique private static final SporranHelper.MethodOverrideCheck sporran$GET_ALL_ENCHANTMENTS_OVERRIDE = SporranHelper.MethodOverrideCheck.of(IItemExtension.class, "getAllEnchantments", ItemStack.class, HolderLookup.RegistryLookup.class);
    @Unique private static final SporranHelper.MethodOverrideCheck sporran$GET_ENCHANTMENT_VALUE_OVERRIDE = SporranHelper.MethodOverrideCheck.of(IItemExtension.class, "getEnchantmentValue", ItemStack.class);
    @Unique private static final SporranHelper.MethodOverrideCheck sporran$IS_PRIMARY_ITEM_FOR_OVERRIDE = SporranHelper.MethodOverrideCheck.of(IItemExtension.class, "isPrimaryItemFor", ItemStack.class, Holder.class);
    @Unique private static final SporranHelper.MethodOverrideCheck sporran$SUPPORTS_ENCHANTMENT_OVERRIDE = SporranHelper.MethodOverrideCheck.of(IItemExtension.class, "supportsEnchantment", ItemStack.class, Holder.class);

    @Inject(method = "getItemEnchantmentLevel", at = @At("HEAD"), cancellable = true)
    private static void sporran$tryHandleDirectEnchantmentLevel(Holder<Enchantment> enchantment, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (!EnchantmentHelperInjection.sporran$shouldUseTagEnchantment.get() && sporran$GET_ENCHANTMENT_LEVEL_OVERRIDE.test(stack.getItem().getClass())) {
            cir.setReturnValue(stack.getEnchantmentLevel(enchantment));
        }
    }

    // Sporran: for an item that does not override getEnchantmentLevel the vanilla body runs (it reads the same
    //  enchantments component as the IItemExtension default); NeoForge's GetEnchantmentLevelEvent still gets to change
    //  its result, as IItemStackExtension#getEnchantmentLevel would.
    @ModifyReturnValue(method = "getItemEnchantmentLevel", at = @At("RETURN"))
    private static int sporran$postEnchantmentLevelEvent(int original, Holder<Enchantment> enchantment, ItemStack stack) {
        if (EnchantmentHelperInjection.sporran$shouldUseTagEnchantment.get() || sporran$GET_ENCHANTMENT_LEVEL_OVERRIDE.test(stack.getItem().getClass())) {
            return original;
        }

        return EventHooks.getEnchantmentLevelSpecific(original, stack, enchantment);
    }

    @CreateStatic
    private static int getTagEnchantmentLevel(Holder<Enchantment> enchantment, ItemStack stack) {
        EnchantmentHelperInjection.sporran$shouldUseTagEnchantment.set(true);
        var value = EnchantmentHelperInjection.getTagEnchantmentLevel(enchantment, stack);
        EnchantmentHelperInjection.sporran$shouldUseTagEnchantment.remove();
        return value;
    }

    @ModifyVariable(method = "runIterationOnItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/enchantment/EnchantmentHelper$EnchantmentVisitor;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/ItemEnchantments;entrySet()Ljava/util/Set;"))
    private static ItemEnchantments sporran$tryGetAllEnchantmentsFromLookup(ItemEnchantments value, @Local(argsOnly = true) ItemStack stack) {
        var lookup = CommonHooks.resolveLookup(Registries.ENCHANTMENT);
        if (lookup != null) {
            if (sporran$GET_ALL_ENCHANTMENTS_OVERRIDE.test(stack.getItem().getClass())) {
                return stack.getAllEnchantments(lookup);
            } else {
                return EventHooks.getAllEnchantmentLevels(value, stack, lookup); // Sporran: Make sure to still call the event.
            }
        }

        return value;
    }

    @Definition(id = "itemEnchantments", local = @Local(type = ItemEnchantments.class))
    @Expression("itemEnchantments != null")
    @ModifyVariable(method = "runIterationOnItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/enchantment/EnchantmentHelper$EnchantmentInSlotVisitor;)V", at = @At("MIXINEXTRAS:EXPRESSION"))
    private static ItemEnchantments sporran$tryGetAllEnchantments(ItemEnchantments value, @Local(argsOnly = true) ItemStack stack, @Local(argsOnly = true) LivingEntity entity) {
        var lookup = entity.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);

        if (sporran$GET_ALL_ENCHANTMENTS_OVERRIDE.test(stack.getItem().getClass())) {
            return stack.getAllEnchantments(lookup);
        } else {
            return EventHooks.getAllEnchantmentLevels(value, stack, lookup); // Sporran: Make sure to still call the event.
        }
    }

    @WrapOperation(method = {"getEnchantmentCost", "selectEnchantment"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;getEnchantmentValue()I"))
    private static int sporran$tryGetEnchantmentValueFromStack(Item instance, Operation<Integer> original, @Local(argsOnly = true) ItemStack stack) {
        if (sporran$GET_ENCHANTMENT_VALUE_OVERRIDE.test(stack.getItem().getClass())) {
            return stack.getEnchantmentValue();
        }

        return original.call(instance);
    }

    @Inject(method = "method_60143", at = @At("HEAD"), cancellable = true)
    private static void sporran$tryCheckIsPrimaryItem(ItemStack itemStack, boolean bl, Holder<Enchantment> holder, CallbackInfoReturnable<Boolean> cir) {
        // Sporran: the IItemExtension default of isPrimaryItemFor builds on supportsEnchantment, so an item that only
        //  overrides that one still takes the NeoForge path. Every other item keeps vanilla's filter (Enchantment#isPrimaryItem).
        Class<?> itemClass = itemStack.getItem().getClass();
        if (sporran$IS_PRIMARY_ITEM_FOR_OVERRIDE.test(itemClass) || sporran$SUPPORTS_ENCHANTMENT_OVERRIDE.test(itemClass)) {
            cir.setReturnValue(itemStack.isPrimaryItemFor(holder));
        }
    }
}
