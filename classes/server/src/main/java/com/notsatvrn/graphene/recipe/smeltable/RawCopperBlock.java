// Original code by MrFishCakes, licensed under (license unknown)
// You can find the original code at (link taken down)

package com.notsatvrn.graphene.recipe.smeltable;

import com.notsatvrn.graphene.config.GrapheneConfig;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;

public class RawCopperBlock {

    public static final NamespacedKey BLASTING = NamespacedKey.crunchy("raw_copper_block_blast_furnace");
    public static final NamespacedKey FURNACE = NamespacedKey.crunchy("raw_copper_block_furnace");

    private static final ItemStack RESULT = new ItemStack(Material.COPPER_BLOCK);
    private static final Material MATERIAL = Material.RAW_COPPER_BLOCK;

    public static final class BlastFurnace extends BlastingRecipe {

        public BlastFurnace() {
            super(BLASTING, RESULT, MATERIAL, GrapheneConfig.smeltRawCopperBlock.experience(), GrapheneConfig.smeltRawCopperBlock.blastCookTime());
        }

    }

    public static final class Furnace extends FurnaceRecipe {

        public Furnace() {
            super(FURNACE, RESULT, MATERIAL, GrapheneConfig.smeltRawCopperBlock.experience(), GrapheneConfig.smeltRawCopperBlock.cookTime());
        }

    }

}

