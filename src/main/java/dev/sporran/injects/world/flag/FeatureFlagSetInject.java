package dev.sporran.injects.world.flag;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.HashCommon;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import dev.sporran.helpers.mixin.CreateInitializer;
import dev.sporran.injections.world.flag.FeatureFlagSetInjection;
import dev.sporran.util.IteratorWrapper;

import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlagUniverse;

@Mixin(FeatureFlagSet.class)
public abstract class FeatureFlagSetInject implements FeatureFlagSetInjection {
    @Unique private static final long[] EMPTY_EXT_MASK = new long[0];

    @Unique private long[] extendedMask = EMPTY_EXT_MASK;

    private FeatureFlagSetInject(@Nullable FeatureFlagUniverse universe, long mask) {
    }

    @CreateInitializer
    private FeatureFlagSetInject(@Nullable FeatureFlagUniverse universe, long mask, long[] extendedMask) {
        this(universe, mask);
        this.extendedMask = extendedMask;
    }

    @ModifyExpressionValue(method = "create", at = @At(value = "NEW", target = "(Lnet/minecraft/world/flag/FeatureFlagUniverse;J)Lnet/minecraft/world/flag/FeatureFlagSet;"))
    private static FeatureFlagSet sporran$addExtendedMaskToFlagSet(FeatureFlagSet original, @Local(argsOnly = true) FeatureFlagUniverse universe, @Local(argsOnly = true) Collection<FeatureFlag> flags) {
        original.sporran$setExtendedMask(computeExtendedMask(universe, 0, 0L, flags));
        return original;
    }

    @WrapOperation(method = {"of(Lnet/minecraft/world/flag/FeatureFlag;)Lnet/minecraft/world/flag/FeatureFlagSet;", "of(Lnet/minecraft/world/flag/FeatureFlag;[Lnet/minecraft/world/flag/FeatureFlag;)Lnet/minecraft/world/flag/FeatureFlagSet;"}, at = @At(value = "FIELD", target = "Lnet/minecraft/world/flag/FeatureFlag;mask:J", opcode = Opcodes.GETFIELD))
    private static long sporran$checkMaskIndexForMask(FeatureFlag instance, Operation<Long> original) {
        return instance.sporran$extMaskIndex() >= 0 ? 0 : original.call(instance);
    }

    @ModifyExpressionValue(method = "of(Lnet/minecraft/world/flag/FeatureFlag;)Lnet/minecraft/world/flag/FeatureFlagSet;", at = @At(value = "NEW", target = "(Lnet/minecraft/world/flag/FeatureFlagUniverse;J)Lnet/minecraft/world/flag/FeatureFlagSet;"))
    private static FeatureFlagSet sporran$addExtendedMaskToFlagSet(FeatureFlagSet original, @Local(argsOnly = true) FeatureFlag flag) {
        original.sporran$setExtendedMask(computeExtendedMask(flag.sporran$universe(), flag.sporran$extMaskIndex(), flag.sporran$mask(), List.of()));
        return original;
    }

    @ModifyExpressionValue(method = "of(Lnet/minecraft/world/flag/FeatureFlag;[Lnet/minecraft/world/flag/FeatureFlag;)Lnet/minecraft/world/flag/FeatureFlagSet;", at = @At(value = "NEW", target = "(Lnet/minecraft/world/flag/FeatureFlagUniverse;J)Lnet/minecraft/world/flag/FeatureFlagSet;"))
    private static FeatureFlagSet sporran$addExtendedMaskToFlagSet(FeatureFlagSet original, @Local(argsOnly = true) FeatureFlag flag, @Local(argsOnly = true) FeatureFlag[] flags) {
        original.sporran$setExtendedMask(computeExtendedMask(flag.sporran$universe(), flag.sporran$extMaskIndex(), flag.sporran$mask(), flags.length == 0 ? List.of() : Arrays.asList(flags)));
        return original;
    }

    @ModifyExpressionValue(method = "computeMask", at = @At(value = "INVOKE", target = "Ljava/lang/Iterable;iterator()Ljava/util/Iterator;"))
    private static <T extends FeatureFlag> Iterator<T> sporran$filterExtendedMasks(Iterator<T> original) {
        return new IteratorWrapper<>(original, flag -> flag.sporran$extMaskIndex() >= 0 ? null : flag);
    }

    @Unique
    private static long[] computeExtendedMask(FeatureFlagUniverse universe, int firstExtIndex, long firstMask, Iterable<FeatureFlag> otherFlags) {
        long[] extMask = EMPTY_EXT_MASK;
        if (firstExtIndex >= 0) {
            extMask = new long[firstExtIndex + 1];
            extMask[firstExtIndex] |= firstMask;
        }

        for (FeatureFlag flag : otherFlags) {
            if (flag.sporran$extMaskIndex() < 0)
                continue;

            if (universe != flag.sporran$universe())
                throw new IllegalStateException("Mismatched feature universe, expected '" + universe + "', but got '" + flag.sporran$universe() + "'");

            if (flag.sporran$extMaskIndex() >= extMask.length)
                extMask = Arrays.copyOfRange(extMask, 0, flag.sporran$extMaskIndex() + 1);

            extMask[flag.sporran$extMaskIndex()] |= flag.sporran$mask();
        }

        return extMask;
    }

    @Expression("(? & ?) != 0")
    @ModifyExpressionValue(method = "contains", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean sporran$checkContainsWithExtendedMask(boolean original, @Local(argsOnly = true) FeatureFlag flag) {
        if (flag.sporran$extMaskIndex() < 0) {
            return original;
        }

        if (this.extendedMask.length > flag.sporran$extMaskIndex()) {
            return (this.extendedMask[flag.sporran$extMaskIndex()] & flag.sporran$mask()) != 0;
        }

        return false;
    }

    @Expression("(? & ~?) == 0")
    @ModifyExpressionValue(method = "isSubsetOf", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean sporran$checkSubsetWithExtendedMask(boolean original, @Local(argsOnly = true) FeatureFlagSet set) {
        int len = Math.max(this.extendedMask.length, set.sporran$extendedMask().length);
        for (int i = 0; i < len; i++) {
            long thisMask = i < this.extendedMask.length ? this.extendedMask[i] : 0;
            long otherMask = i < set.sporran$extendedMask().length ? set.sporran$extendedMask()[i] : 0;
            if ((thisMask & ~otherMask) != 0)
                return false;
        }

        return original;
    }

    @Expression("(? & ?) != 0")
    @ModifyExpressionValue(method = "intersects", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean sporran$checkIntersectsWithExtendedMask(boolean original, @Local(argsOnly = true) FeatureFlagSet set) {
        int len = Math.min(this.extendedMask.length, set.sporran$extendedMask().length);
        for (int i = 0; i < len; i++) {
            long thisMask = this.extendedMask[i];
            long otherMask = set.sporran$extendedMask()[i];
            if ((thisMask & otherMask) != 0)
                return true;
        }

        return original;
    }

    @ModifyExpressionValue(method = "join", at = @At(value = "NEW", target = "(Lnet/minecraft/world/flag/FeatureFlagUniverse;J)Lnet/minecraft/world/flag/FeatureFlagSet;"))
    private FeatureFlagSet sporran$addExtendedMaskToSet(FeatureFlagSet original, @Local(argsOnly = true) FeatureFlagSet other) {
        long[] extMask = EMPTY_EXT_MASK;
        if (this.extendedMask.length > 0 || other.sporran$extendedMask().length > 0) {
            extMask = new long[Math.max(this.extendedMask.length, other.sporran$extendedMask().length)];
            for (int i = 0; i < extMask.length; i++) {
                long thisMask = i < this.extendedMask.length ? this.extendedMask[i] : 0;
                long otherMask = i < other.sporran$extendedMask().length ? other.sporran$extendedMask()[i] : 0;
                extMask[i] = thisMask | otherMask;
            }
        }

        original.sporran$setExtendedMask(extMask);
        return original;
    }

    @ModifyExpressionValue(method = "subtract", at = @At(value = "NEW", target = "(Lnet/minecraft/world/flag/FeatureFlagUniverse;J)Lnet/minecraft/world/flag/FeatureFlagSet;"))
    private FeatureFlagSet sporran$removeExtendedMaskFromSet(FeatureFlagSet original, @Local(argsOnly = true) FeatureFlagSet other) {
        long[] extMask = EMPTY_EXT_MASK;
        if (this.extendedMask.length > 0 || other.sporran$extendedMask().length > 0) {
            extMask = new long[this.extendedMask.length];
            for (int i = 0; i < extMask.length; i++) {
                long otherMask = i < other.sporran$extendedMask().length ? other.sporran$extendedMask()[i] : 0;
                extMask[i] = this.extendedMask[i] & ~otherMask;
            }
        }

        original.sporran$setExtendedMask(extMask);
        return original;
    }

    @Definition(id = "mask", field = "Lnet/minecraft/world/flag/FeatureFlagSet;mask:J")
    @Definition(id = "featureFlagSet", local = @Local(type = FeatureFlagSet.class))
    @Expression("this.mask == featureFlagSet.mask")
    @ModifyExpressionValue(method = "equals", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean sporran$checkExtendedMaskMatches(boolean original, @Local FeatureFlagSet other) {
        return original && Arrays.equals(this.extendedMask, other.sporran$extendedMask());
    }

    @ModifyReturnValue(method = "hashCode", at = @At("RETURN"))
    private int sporran$addExtendedMaskToHashCode(int original) {
        for (long extMask : this.extendedMask) {
            original = 13 * original + (int) HashCommon.mix(extMask);
        }

        return original;
    }

    @Override
    public long[] sporran$extendedMask() {
        return this.extendedMask;
    }

    @Override
    public void sporran$setExtendedMask(long[] extendedMask) {
        this.extendedMask = extendedMask;
    }
}
