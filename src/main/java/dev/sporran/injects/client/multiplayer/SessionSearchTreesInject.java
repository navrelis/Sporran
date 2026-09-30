package dev.sporran.injects.client.multiplayer;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.multiplayer.SessionSearchTrees;
import net.minecraft.client.searchtree.SearchTree;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.CreativeModeTabSearchRegistry;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import dev.sporran.injections.client.multiplayer.SessionSearchTreesInjection;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

@Mixin(SessionSearchTrees.class)
public abstract class SessionSearchTreesInject implements SessionSearchTreesInjection {
    @Shadow public static SessionSearchTrees.Key CREATIVE_TAGS;
    @Shadow public abstract void updateCreativeTags(List<ItemStack> items);
    @Shadow public abstract void updateCreativeTooltips(HolderLookup.Provider registries, List<ItemStack> items);

    @Shadow
    private CompletableFuture<SearchTree<ItemStack>> creativeByTagSearch;
    @Shadow
    private CompletableFuture<SearchTree<ItemStack>> creativeByNameSearch;
    @Unique private SessionSearchTrees.Key sporran$key;

    @Override
    public void updateCreativeTags(List<ItemStack> items, SessionSearchTrees.Key key) {
        this.sporran$key = key;
        this.updateCreativeTags(items);
        this.sporran$key = null;
    }

    @ModifyExpressionValue(method = "updateCreativeTags", at = @At(value = "FIELD", target = "Lnet/minecraft/client/multiplayer/SessionSearchTrees;CREATIVE_TAGS:Lnet/minecraft/client/multiplayer/SessionSearchTrees$Key;"))
    private SessionSearchTrees.Key sporran$useCustomKeyIfAvailable(SessionSearchTrees.Key original) {
        if (this.sporran$key != null)
            return this.sporran$key;

        return original;
    }

    @ModifyExpressionValue(method = "method_60368", at = @At(value = "FIELD", target = "Lnet/minecraft/client/multiplayer/SessionSearchTrees;creativeByTagSearch:Ljava/util/concurrent/CompletableFuture;", opcode = Opcodes.GETFIELD))
    private CompletableFuture<SearchTree<ItemStack>> sporran$useNeoTagSearchTree(CompletableFuture<SearchTree<ItemStack>> original) {
        if (this.sporran$key != null)
            return CreativeModeTabSearchRegistry.getTagSearchTree(this.sporran$key);

        return original;
    }

    @WrapOperation(method = "method_60368", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/CompletableFuture;supplyAsync(Ljava/util/function/Supplier;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"))
    private <U> CompletableFuture<U> sporran$storeTagSearchTree(Supplier<U> supplier, Executor executor, Operation<CompletableFuture<U>> original) {
        var future = original.call(supplier, executor);

        if (this.sporran$key != null) {
            CreativeModeTabSearchRegistry.putTagSearchTree(this.sporran$key, (CompletableFuture<SearchTree<ItemStack>>) future);
            return (CompletableFuture<U>) this.creativeByTagSearch;
        }

        return future;
    }

    @Override
    public SearchTree<ItemStack> creativeTagSearch(SessionSearchTrees.Key key) {
        return CreativeModeTabSearchRegistry.getTagSearchTree(key).join();
    }

    @Override
    public void updateCreativeTooltips(HolderLookup.Provider provider, List<ItemStack> items, SessionSearchTrees.Key key) {
        this.sporran$key = key;
        this.updateCreativeTooltips(provider, items);
        this.sporran$key = null;
    }

    @ModifyExpressionValue(method = "updateCreativeTooltips", at = @At(value = "FIELD", target = "Lnet/minecraft/client/multiplayer/SessionSearchTrees;CREATIVE_NAMES:Lnet/minecraft/client/multiplayer/SessionSearchTrees$Key;"))
    private SessionSearchTrees.Key sporran$useCustomKeyOnNameIfAvailable(SessionSearchTrees.Key original) {
        if (this.sporran$key != null)
            return this.sporran$key;

        return original;
    }

    @ModifyExpressionValue(method = "method_60369", at = @At(value = "FIELD", target = "Lnet/minecraft/client/multiplayer/SessionSearchTrees;creativeByNameSearch:Ljava/util/concurrent/CompletableFuture;", opcode = Opcodes.GETFIELD))
    private CompletableFuture<SearchTree<ItemStack>> sporran$useNeoNameSearchTree(CompletableFuture<SearchTree<ItemStack>> original) {
        if (this.sporran$key != null)
            return CreativeModeTabSearchRegistry.getNameSearchTree(this.sporran$key);

        return original;
    }

    @WrapOperation(method = "method_60369", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/CompletableFuture;supplyAsync(Ljava/util/function/Supplier;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"))
    private <U> CompletableFuture<U> sporran$storeNameSearchTree(Supplier<U> supplier, Executor executor, Operation<CompletableFuture<U>> original) {
        var future = original.call(supplier, executor);

        if (this.sporran$key != null) {
            CreativeModeTabSearchRegistry.putNameSearchTree(this.sporran$key, (CompletableFuture<SearchTree<ItemStack>>) future);
            return (CompletableFuture<U>) this.creativeByNameSearch;
        }

        return future;
    }

    @Override
    public SearchTree<ItemStack> creativeNameSearch(SessionSearchTrees.Key key) {
        return CreativeModeTabSearchRegistry.getNameSearchTree(key).join();
    }
}
