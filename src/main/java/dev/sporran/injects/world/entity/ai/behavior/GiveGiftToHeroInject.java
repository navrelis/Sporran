package dev.sporran.injects.world.entity.ai.behavior;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.behavior.GiveGiftToHero;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(GiveGiftToHero.class)
public abstract class GiveGiftToHeroInject {
    @ModifyExpressionValue(method = "getItemToThrow", at = @At(value = "INVOKE", target = "Ljava/util/Map;containsKey(Ljava/lang/Object;)Z"))
    private boolean sporran$justAlwaysReturnTrue(boolean original, @Local VillagerProfession profession) {
        return original || BuiltInRegistries.VILLAGER_PROFESSION.wrapAsHolder(profession).getData(NeoForgeDataMaps.RAID_HERO_GIFTS) != null;
    }

    @WrapOperation(method = "getItemToThrow", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"))
    private <K, V> V sporran$tryUseDataMap(Map<K, V> instance, Object o, Operation<V> original, @Local VillagerProfession profession) {
        var existing = original.call(instance, o);

        if (existing != null)
            return existing;

        var gift = BuiltInRegistries.VILLAGER_PROFESSION.wrapAsHolder(profession).getData(NeoForgeDataMaps.RAID_HERO_GIFTS);

        if (gift != null) {
            return (V) gift.lootTable();
        }

        return null;
    }
}
