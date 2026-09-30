// TRACKED HASH: 463588f2acf28725b91608efe6c2270022c2dde9
package dev.sporran.injects.world.level;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.neoforged.neoforge.common.extensions.IEntityExtension;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import dev.sporran.util.SporranHelper;

@Mixin(NaturalSpawner.class)
public abstract class NaturalSpawnerInject {
    // Sporran: runs for every loaded entity whenever the mob caps are counted, resolve the override check only once.
    @Unique private static final SporranHelper.MethodOverrideCheck sporran$GET_CLASSIFICATION_OVERRIDE = SporranHelper.MethodOverrideCheck.of(IEntityExtension.class, "getClassification", boolean.class);

    @WrapOperation(method = "createState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityType;getCategory()Lnet/minecraft/world/entity/MobCategory;"))
    private static MobCategory sporran$tryUseNeoClassification(EntityType<?> instance, Operation<MobCategory> original, @Local Entity entity) {
        if (sporran$GET_CLASSIFICATION_OVERRIDE.test(entity.getClass())) {
            return entity.getClassification(true);
        }

        return original.call(instance);
    }

    @WrapOperation(
            method = "spawnCategoryForPosition(Lnet/minecraft/world/entity/MobCategory;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/NaturalSpawner$SpawnPredicate;Lnet/minecraft/world/level/NaturalSpawner$AfterSpawnCallback;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;getMaxSpawnClusterSize()I")
    )
    private static int sporran$getMaxPackSizeEvent(Mob instance, Operation<Integer> original) {
        return EventHooks.sporran$getMaxSpawnClusterSize(instance, original);
    }

    @WrapOperation(method = "isValidPositionForMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;checkSpawnRules(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/entity/MobSpawnType;)Z"))
    private static boolean sporran$useNeoCheckSpawnPosition(Mob instance, LevelAccessor levelAccessor, MobSpawnType mobSpawnType, Operation<Boolean> original, @Share("result") LocalRef<MobSpawnEvent.PositionCheck.Result> result) {
        result.set(EventHooks.sporran$checkSpawnPosition(instance, (ServerLevelAccessor) levelAccessor, mobSpawnType));

        if (result.get() != MobSpawnEvent.PositionCheck.Result.DEFAULT) {
            return result.get() == MobSpawnEvent.PositionCheck.Result.SUCCEED;
        }

        return original.call(instance, levelAccessor, mobSpawnType);
    }

    @WrapOperation(method = "isValidPositionForMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;checkSpawnObstruction(Lnet/minecraft/world/level/LevelReader;)Z"))
    private static boolean sporran$useCheckSpawnPosResult(Mob instance, LevelReader levelReader, Operation<Boolean> original, @Share("result") LocalRef<MobSpawnEvent.PositionCheck.Result> result) {
        if (result.get() != MobSpawnEvent.PositionCheck.Result.DEFAULT)
            return result.get() == MobSpawnEvent.PositionCheck.Result.SUCCEED;

        return original.call(instance, levelReader);
    }

    @ModifyExpressionValue(method = "mobsAt", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/levelgen/structure/structures/NetherFortressStructure;FORTRESS_ENEMIES:Lnet/minecraft/util/random/WeightedRandomList;"))
    private static WeightedRandomList<MobSpawnSettings.SpawnerData> sporran$tryUseNeoMonsterSpawns(WeightedRandomList<MobSpawnSettings.SpawnerData> original, @Local(argsOnly = true) ServerLevel level, @Local(argsOnly = true) MobCategory category, @Local(argsOnly = true) BlockPos pos) {
        var monsterSpawns = level.registryAccess().registryOrThrow(Registries.STRUCTURE)
            .getOrThrow(BuiltinStructures.FORTRESS)
            .spawnOverrides()
            .get(MobCategory.MONSTER);

        // TODO: this might be mod-incompatible...
        if (monsterSpawns != null) {
            return EventHooks.getPotentialSpawns(level, category, pos, monsterSpawns.spawns());
        }

        return EventHooks.getPotentialSpawns(level, category, pos, original);
    }

    @ModifyExpressionValue(method = "mobsAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/ChunkGenerator;getMobsAt(Lnet/minecraft/core/Holder;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/entity/MobCategory;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/util/random/WeightedRandomList;"))
    private static WeightedRandomList<MobSpawnSettings.SpawnerData> sporran$checkNeoPotentialSpawns(WeightedRandomList<MobSpawnSettings.SpawnerData> original, @Local(argsOnly = true) ServerLevel level, @Local(argsOnly = true) MobCategory category, @Local(argsOnly = true) BlockPos pos) {
        return EventHooks.getPotentialSpawns(level, category, pos, original);
    }

    @WrapOperation(method = "spawnMobsForChunkGeneration", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;checkSpawnRules(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/entity/MobSpawnType;)Z"))
    private static boolean sporran$useNeoCheckSpawnPositionChunkGen(Mob instance, LevelAccessor levelAccessor, MobSpawnType mobSpawnType, Operation<Boolean> original, @Share("result") LocalRef<MobSpawnEvent.PositionCheck.Result> result) {
        result.set(EventHooks.sporran$checkSpawnPosition(instance, (ServerLevelAccessor) levelAccessor, mobSpawnType));

        if (result.get() != MobSpawnEvent.PositionCheck.Result.DEFAULT) {
            var value = result.get() == MobSpawnEvent.PositionCheck.Result.SUCCEED;
            if (value)
                result.set(MobSpawnEvent.PositionCheck.Result.DEFAULT);

            return value;
        }

        result.set(MobSpawnEvent.PositionCheck.Result.DEFAULT);
        return original.call(instance, levelAccessor, mobSpawnType);
    }

    @WrapOperation(method = "spawnMobsForChunkGeneration", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;checkSpawnObstruction(Lnet/minecraft/world/level/LevelReader;)Z"))
    private static boolean sporran$useCheckSpawnPosResultChunkGen(Mob instance, LevelReader levelReader, Operation<Boolean> original, @Share("result") LocalRef<MobSpawnEvent.PositionCheck.Result> result) {
        if (result.get() != MobSpawnEvent.PositionCheck.Result.DEFAULT) {
            var value = result.get() == MobSpawnEvent.PositionCheck.Result.SUCCEED;
            if (value)
                result.set(MobSpawnEvent.PositionCheck.Result.DEFAULT);

            return value;
        }

        result.set(MobSpawnEvent.PositionCheck.Result.DEFAULT);
        return original.call(instance, levelReader);
    }
}