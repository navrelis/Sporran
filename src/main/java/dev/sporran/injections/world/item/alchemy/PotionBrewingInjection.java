package dev.sporran.injections.world.item.alchemy;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import net.neoforged.neoforge.common.brewing.BrewingRecipeRegistry;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;
import dev.sporran.util.SporranHelper;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;

public interface PotionBrewingInjection {
    AtomicReference<RegistryAccess> sporran$registryAccess = new AtomicReference<>(RegistryAccess.EMPTY);

    static PotionBrewing bootstrap(FeatureFlagSet enabledFeatures, RegistryAccess registryAccess) {
        sporran$registryAccess.set(registryAccess);
        return PotionBrewing.bootstrap(enabledFeatures);
    }

    default boolean isInput(ItemStack stack) {
        throw SporranHelper.createMixinException(PotionBrewingInjection.class, "isInput");
    }

    default List<IBrewingRecipe> neoforge$getRecipes() {
        throw SporranHelper.createMixinException(PotionBrewingInjection.class, "getRecipes");
    }

    default void sporran$setBrewingRegistry(BrewingRecipeRegistry registry) {
        throw SporranHelper.createMixinException(PotionBrewingInjection.class, "sporran$setBrewingRegistry");
    }

    interface BuilderInjection {
        default void addRecipe(Ingredient input, Ingredient ingredient, ItemStack output) {
            throw SporranHelper.createMixinException(BuilderInjection.class, "addRecipe");
        }

        default void addRecipe(IBrewingRecipe recipe) {
            throw SporranHelper.createMixinException(BuilderInjection.class, "addRecipe");
        }
    }
}
