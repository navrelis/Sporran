/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.server.advancements;

import it.unimi.dsi.fastutil.Stack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementNode;
import net.minecraft.server.advancements.AdvancementVisibilityEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import dev.sporran.helpers.mixin.CreateStatic;

import java.util.function.Predicate;

@Mixin(AdvancementVisibilityEvaluator.class)
public abstract class AdvancementVisibilityEvaluatorInject {
    @Shadow
    private static boolean evaluateVisibility(AdvancementNode advancementNode, Stack<AdvancementVisibilityEvaluator.VisibilityRule> stack, Predicate<AdvancementNode> predicate, AdvancementVisibilityEvaluator.Output output) {
        throw new IllegalStateException();
    }

    @CreateStatic
    private static boolean isVisible(AdvancementNode advancement, Predicate<AdvancementNode> test) {
        Stack<AdvancementVisibilityEvaluator.VisibilityRule> stack = new ObjectArrayList<>();

        for (int i = 0; i <= 2; ++i) {
            stack.push(AdvancementVisibilityEvaluator.VisibilityRule.NO_CHANGE);
        }

        return evaluateVisibility(advancement.root(), stack, test, (a, b) -> {});
    }
}
