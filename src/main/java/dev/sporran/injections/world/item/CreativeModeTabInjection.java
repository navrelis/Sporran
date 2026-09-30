package dev.sporran.injections.world.item;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import dev.sporran.mixin.CreativeModeTabAccessor;
import dev.sporran.mixin.world.item.CreativeModeTabBuilderAccessor;
import dev.sporran.util.SporranHelper;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;

public interface CreativeModeTabInjection {
    static CreativeModeTab create(CreativeModeTab.Builder builder) {
        var tab = CreativeModeTabAccessor.createCreativeModeTab(((CreativeModeTabBuilderAccessor) builder).getRow(), ((CreativeModeTabBuilderAccessor) builder).getColumn(), ((CreativeModeTabBuilderAccessor) builder).getType(), ((CreativeModeTabBuilderAccessor) builder).getDisplayName(), ((CreativeModeTabBuilderAccessor) builder).getIconGenerator(), ((CreativeModeTabBuilderAccessor) builder).getDisplayItemsGenerator());
        tab.sporran$assignValues(builder);

        return tab;
    }

    static CreativeModeTab.Builder builder() {
        return new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 0);
    }

    // Helper method for both create() here and <init> in the mixin
    default void sporran$assignValues(CreativeModeTab.Builder builder) {}
    default void sporran$setScrollerSprite(ResourceLocation location) {}

    default ResourceLocation getScrollerSprite() {
        throw SporranHelper.createMixinException(CreativeModeTabInjection.class, "getScrollerSprite");
    }

    default boolean hasSearchBar() {
        throw SporranHelper.createMixinException(CreativeModeTabInjection.class, "hasSearchBar");
    }

    default int getSearchBarWidth() {
        throw SporranHelper.createMixinException(CreativeModeTabInjection.class, "getSearchBarWidth");
    }

    default ResourceLocation getTabsImage() {
        throw SporranHelper.createMixinException(CreativeModeTabInjection.class, "getTabsImage");
    }

    default int getLabelColor() {
        throw SporranHelper.createMixinException(CreativeModeTabInjection.class, "getLabelColor");
    }

    default int getSlotColor() {
        throw SporranHelper.createMixinException(CreativeModeTabInjection.class, "");
    }

    default List<ResourceLocation> sporran$getTabsBefore() {
        throw SporranHelper.createMixinException(CreativeModeTabInjection.class, "sporran$getTabsBefore");
    }

    default List<ResourceLocation> sporran$getTabsAfter() {
        throw SporranHelper.createMixinException(CreativeModeTabInjection.class, "sporran$getTabsAfter");
    }

    interface BuilderInjection {
        default CreativeModeTab.Builder withScrollBarSpriteLocation(ResourceLocation location) {
            throw SporranHelper.createMixinException(CreativeModeTabInjection.BuilderInjection.class, "withScrollBarSpriteLocation");
        }

        default CreativeModeTab.Builder withSearchBar() {
            throw SporranHelper.createMixinException(CreativeModeTabInjection.BuilderInjection.class, "withSearchBar");
        }

        default CreativeModeTab.Builder withSearchBar(int searchBarWidth) {
            throw SporranHelper.createMixinException(CreativeModeTabInjection.BuilderInjection.class, "withSearchBar");
        }

        default CreativeModeTab.Builder withTabsImage(ResourceLocation tabsImage) {
            throw SporranHelper.createMixinException(CreativeModeTabInjection.BuilderInjection.class, "withTabsImage");
        }

        default CreativeModeTab.Builder withLabelColor(int labelColor) {
            throw SporranHelper.createMixinException(CreativeModeTabInjection.BuilderInjection.class, "withLabelColor");
        }

        default CreativeModeTab.Builder withSlotColor(int slotColor) {
            throw SporranHelper.createMixinException(CreativeModeTabInjection.BuilderInjection.class, "withSlotColor");
        }

        default CreativeModeTab.Builder withTabFactory(Function<CreativeModeTab.Builder, CreativeModeTab> factory) {
            throw SporranHelper.createMixinException(CreativeModeTabInjection.BuilderInjection.class, "withTabFactory");
        }

        default CreativeModeTab.Builder withTabsBefore(ResourceKey<CreativeModeTab>... tabs) {
            throw SporranHelper.createMixinException(CreativeModeTabInjection.BuilderInjection.class, "withTabsBefore");
        }

        default CreativeModeTab.Builder withTabsAfter(ResourceKey<CreativeModeTab>... tabs) {
            throw SporranHelper.createMixinException(CreativeModeTabInjection.BuilderInjection.class, "withTabsAfter");
        }

        default CreativeModeTab.Builder withTabsBefore(ResourceLocation... tabs) {
            throw SporranHelper.createMixinException(CreativeModeTabInjection.BuilderInjection.class, "withTabsBefore");
        }

        default CreativeModeTab.Builder withTabsAfter(ResourceLocation... tabs) {
            throw SporranHelper.createMixinException(CreativeModeTabInjection.BuilderInjection.class, "withTabsAfter");
        }

        default CreativeModeTab.Builder displayItems(Collection<? extends Holder<? extends ItemLike>> collection) {
            throw SporranHelper.createMixinException(CreativeModeTabInjection.BuilderInjection.class, "displayItems");
        }

        default boolean sporran$hasSearchBar() {
            throw SporranHelper.createMixinException(CreativeModeTabInjection.BuilderInjection.class, "sporran$hasSearchBar");
        }

        default int sporran$searchBarWidth() {
            throw SporranHelper.createMixinException(CreativeModeTabInjection.BuilderInjection.class, "sporran$searchBarWidth");
        }

        default ResourceLocation sporran$getTabsImage() {
            throw SporranHelper.createMixinException(CreativeModeTabInjection.BuilderInjection.class, "sporran$getTabsImage");
        }

        default int sporran$labelColor() {
            throw SporranHelper.createMixinException(CreativeModeTabInjection.BuilderInjection.class, "sporran$labelColor");
        }

        default int sporran$slotColor() {
            throw SporranHelper.createMixinException(CreativeModeTabInjection.BuilderInjection.class, "sporran$slotColor");
        }

        default Function<CreativeModeTab.Builder, CreativeModeTab> sporran$getTabFactory() {
            throw SporranHelper.createMixinException(CreativeModeTabInjection.BuilderInjection.class, "sporran$getTabFactory");
        }

        default List<ResourceLocation> sporran$getTabsBefore() {
            throw SporranHelper.createMixinException(CreativeModeTabInjection.BuilderInjection.class, "sporran$getTabsBefore");
        }

        default List<ResourceLocation> sporran$getTabsAfter() {
            throw SporranHelper.createMixinException(CreativeModeTabInjection.BuilderInjection.class, "sporran$getTabsAfter");
        }

        default ResourceLocation sporran$scrollerSpriteLocation() {
            throw SporranHelper.createMixinException(CreativeModeTabInjection.BuilderInjection.class, "sporran$scrollerSpriteLocation");
        }
    }
}
