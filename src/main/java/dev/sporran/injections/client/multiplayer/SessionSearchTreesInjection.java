package dev.sporran.injections.client.multiplayer;

import net.minecraft.client.multiplayer.SessionSearchTrees;
import net.minecraft.client.searchtree.SearchTree;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import dev.sporran.util.SporranHelper;

import java.util.List;

public interface SessionSearchTreesInjection {
    default void updateCreativeTags(List<ItemStack> items, SessionSearchTrees.Key key) {
        throw SporranHelper.createMixinException(SessionSearchTreesInjection.class, "updateCreativeTags");
    }

    default SearchTree<ItemStack> creativeTagSearch(SessionSearchTrees.Key key) {
        throw SporranHelper.createMixinException(SessionSearchTreesInjection.class, "creativeTagSearch");
    }

    default void updateCreativeTooltips(HolderLookup.Provider provider, List<ItemStack> items, SessionSearchTrees.Key key) {
        throw SporranHelper.createMixinException(SessionSearchTreesInjection.class, "updateCreativeTooltips");
    }

    default SearchTree<ItemStack> creativeNameSearch(SessionSearchTrees.Key key) {
        throw SporranHelper.createMixinException(SessionSearchTreesInjection.class, "creativeNameSearch");
    }
}
