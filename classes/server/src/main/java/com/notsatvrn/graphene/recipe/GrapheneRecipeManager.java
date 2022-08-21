// Original code by MrFishCakes, licensed under (license unknown)
// You can find the original code at (link taken down)

package com.notsatvrn.graphene.recipe;

import com.notsatvrn.graphene.config.GrapheneConfig;
import com.notsatvrn.graphene.recipe.smeltable.RawCopperBlock;
import com.notsatvrn.graphene.recipe.smeltable.RawGoldBlock;
import com.notsatvrn.graphene.recipe.smeltable.RawIronBlock;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

public final class GrapheneRecipeManager {

    private static final AtomicBoolean INITIATED = new AtomicBoolean(false);
    private static GrapheneRecipeManager instance;

    public static GrapheneRecipeManager instance() {
        return instance != null ? instance : new GrapheneRecipeManager();
    }

    private GrapheneRecipeManager() {
        if (!INITIATED.compareAndSet(false, true)) return;

        instance = this;

        if (GrapheneConfig.smeltRawCopperBlock.enabled()) {
            registerRecipe(RawCopperBlock.BLASTING, new RawCopperBlock.BlastFurnace());
            registerRecipe(RawCopperBlock.FURNACE, new RawCopperBlock.Furnace());
        }

        if (GrapheneConfig.smeltRawGoldBlock.enabled()) {
            registerRecipe(RawGoldBlock.BLASTING, new RawGoldBlock.BlastFurnace());
            registerRecipe(RawGoldBlock.FURNACE, new RawGoldBlock.Furnace());
        }

        if (GrapheneConfig.smeltRawIronBlock.enabled()) {
            registerRecipe(RawIronBlock.BLASTING, new RawIronBlock.BlastFurnace());
            registerRecipe(RawIronBlock.FURNACE, new RawIronBlock.Furnace());
        }
    }

    public void registerRecipe(@NotNull NamespacedKey key, @NotNull Recipe recipe) {
        if (!Bukkit.addRecipe(recipe)) {
            Bukkit.getLogger().log(Level.SEVERE, String.format("Unable to register the recipe '%s'", key.getKey()));
        }
    }

}

