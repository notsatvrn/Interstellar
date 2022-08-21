// Original code by MrFishCakes, licensed under (license unknown)
// You can find the original code at (link taken down)

package com.notsatvrn.graphene;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * Class for creating skulls easily and applying {@link SkullMeta}
 *
 * @author MrFishCales
 */
public class SkullBuilder {

    /**
     * Enum for managing skull types
     */
    public enum SkullType {

        CREEPER(Material.CREEPER_HEAD),
        DRAGON(Material.DRAGON_HEAD),
        PLAYER(Material.PLAYER_HEAD),
        SKELETON(Material.SKELETON_SKULL),
        WITHER_SKELETON(Material.WITHER_SKELETON_SKULL),
        ZOMBIE(Material.ZOMBIE_HEAD);

        private final Material material;

        SkullType(@NotNull Material material) {
            this.material = material;
        }

        /**
         * Returns the material that is used to make the skull
         *
         * @return {@link Material} type for the skull
         */
        public Material getMaterial() {
            return material;
        }

    }

    private final SkullType skullType;
    private final ItemStack itemStack;
    private final SkullMeta skullMeta;

    /**
     * Create a new instance of the class
     *
     * @param skullType {@link SkullType} that should be used when creating the {@link ItemStack}
     */
    public SkullBuilder(@NotNull SkullType skullType) {
        this.skullType = skullType;
        this.itemStack = new ItemStack(skullType.getMaterial());
        this.skullMeta = (SkullMeta) itemStack.getItemMeta();
    }

    /**
     * Create a default new instance of the class
     * <p>
     * This will create a {@link SkullType#PLAYER} by default
     *
     * @see Material#PLAYER_HEAD
     */
    public SkullBuilder() {
        this(SkullType.PLAYER);
    }

    /**
     * Set the {@link OfflinePlayer} owner of the skull
     *
     * @param player {@link OfflinePlayer} to set the value as
     * @return Updated instance
     */
    public SkullBuilder setOfflinePlayer(@NotNull OfflinePlayer player) {
        if (skullType != SkullType.PLAYER) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot assign player value to non-player skull", new IllegalArgumentException(
                    "Cannot assign player value to a non-player skull"));
            return this;
        }

        skullMeta.setOwningPlayer(player);

        return this;
    }

    /**
     * Set a custom texture to the skull.
     * <p>
     * This allows for more customisation where heads are needed
     *
     * @param value     Skin value
     * @param signature Skin signature
     * @return Updated instance
     */
    public SkullBuilder setCustomTexture(@NotNull String value, @Nullable String signature) {
        if (skullType != SkullType.PLAYER) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot assign player skin value to non-player skull", new IllegalArgumentException(
                    "Cannot assign player skin value to a non-player skull"));
            return this;
        }

        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        profile.setProperty(new ProfileProperty("textures", value, signature));

        skullMeta.setPlayerProfile(profile);

        return this;
    }

    /**
     * Set a custom texture to the skull.
     * <p>
     * This allows for more customisation where heads are needed
     *
     * @param value Skin value
     * @return Updated instance
     * @see SkullBuilder#setCustomTexture(String, String)
     */
    public SkullBuilder setCustomTexture(@NotNull String value) {
        return setCustomTexture(value, null);
    }

    /**
     * Apply custom {@link SkullMeta} to the item
     *
     * @param consumer {@link SkullMeta} to apply
     * @return Updated instance
     */
    public SkullBuilder applyItemMeta(@NotNull Consumer<SkullMeta> consumer) {
        consumer.accept(skullMeta);

        return this;
    }

    /**
     * Get the final {@link ItemStack} for the skull
     *
     * @return Skull as item
     */
    public ItemStack build() {
        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }

}

