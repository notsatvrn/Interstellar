package com.notsatvrn.graphene;

import org.apache.commons.lang.BooleanUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static com.notsatvrn.graphene.GrapheneConfig.log;

@SuppressWarnings("unused")
public class GrapheneWorldConfig {
    private final String worldName;

    public GrapheneWorldConfig(String worldName) {
        this.worldName = worldName;
        init();
    }

    public void init() {
        log("-------- World Settings For [" + worldName + "] --------");
        GrapheneConfig.readConfig(GrapheneWorldConfig.class, this);
    }

    private void set(String path, Object val) {
        GrapheneConfig.config.addDefault("world-settings.default." + path, val);
        GrapheneConfig.config.set("world-settings.default." + path, val);
        if (GrapheneConfig.config.get("world-settings." + worldName + "." + path) != null) {
            GrapheneConfig.config.addDefault("world-settings." + worldName + "." + path, val);
            GrapheneConfig.config.set("world-settings." + worldName + "." + path, val);
        }
    }

    private ConfigurationSection getConfigurationSection(String path) {
        ConfigurationSection section = GrapheneConfig.config.getConfigurationSection("world-settings." + worldName + "." + path);
        return section != null ? section : GrapheneConfig.config.getConfigurationSection("world-settings.default." + path);
    }

    private String getString(String path, String def) {
        GrapheneConfig.config.addDefault("world-settings.default." + path, def);
        return GrapheneConfig.config.getString("world-settings." + worldName + "." + path, GrapheneConfig.config.getString("world-settings.default." + path));
    }

    private boolean getBoolean(String path, boolean def) {
        GrapheneConfig.config.addDefault("world-settings.default." + path, def);
        return GrapheneConfig.config.getBoolean("world-settings." + worldName + "." + path, GrapheneConfig.config.getBoolean("world-settings.default." + path));
    }

    private boolean getBoolean(String path, Predicate<Boolean> predicate) {
        String val = getString(path, "default").toLowerCase();
        Boolean bool = BooleanUtils.toBooleanObject(val, "true", "false", "default");
        return predicate.test(bool);
    }

    private double getDouble(String path, double def) {
        GrapheneConfig.config.addDefault("world-settings.default." + path, def);
        return GrapheneConfig.config.getDouble("world-settings." + worldName + "." + path, GrapheneConfig.config.getDouble("world-settings.default." + path));
    }

    private int getInt(String path, int def) {
        GrapheneConfig.config.addDefault("world-settings.default." + path, def);
        return GrapheneConfig.config.getInt("world-settings." + worldName + "." + path, GrapheneConfig.config.getInt("world-settings.default." + path));
    }

    private <T> List<?> getList(String path, T def) {
        GrapheneConfig.config.addDefault("world-settings.default." + path, def);
        return GrapheneConfig.config.getList("world-settings." + worldName + "." + path, GrapheneConfig.config.getList("world-settings.default." + path));
    }

    private Map<String, Object> getMap(String path, Map<String, Object> def) {
        final Map<String, Object> fallback = GrapheneConfig.getMap("world-settings.default." + path, def);
        final Map<String, Object> value = GrapheneConfig.getMap("world-settings." + worldName + "." + path, null);
        return value.isEmpty() ? fallback : value;
    }

    // Beheading Settings - Crunchy
    public static boolean beheadingEnabled = false;
    public static double beheadingChance = 0.03D;
    public static List<Item> beheadingItems = new ArrayList<Item>();

    private void beheadingSettings() {
        beheadingEnabled = getBoolean("beheading.enabled", beheadingEnabled);
        beheadingChance = getDouble("beheading.chance", beheadingChance);
        getList("beheading.items", List.of(
                "minecraft:diamond_axe",
                "minecraft:netherite_axe",
                "minecraft:diamond_sword",
                "minecraft:netherite_sword"
        )).forEach(key -> {
            Item item = Registry.ITEM.get(new ResourceLocation(key.toString()));
            if (item != Items.AIR) beheadingItems.add(item);
        });
    }

    public boolean isValidBeheadingItem(@Nullable Player player) {
        return player != null && beheadingItems.contains(CraftItemStack.asNMSCopy(player.getInventory().getItemInMainHand()).getItem());
    }

    // Chicken Settings
    public boolean chickenAvoidFox = true;
    public float chickenAvoidFoxDistance = 6.0D;
    public double chickenAvoidFoxSlow = 1.0D;
    public double chickenAvoidFoxFast = 1.5D;

    private void chickenSettings() {
        this.chickenAvoidFox = getBoolean("mobs.chicken.avoid-fox.enabled", chickenAvoidFox);
        this.chickenAvoidFoxDistance = (float) getDouble("mobs.chicken.avoid-fox.distance", chickenAvoidFoxDistance);
        this.chickenAvoidFoxSlow = getDouble("mobs.chicken.avoid-fox.slow-speed", chickenAvoidFoxSlow);
        this.chickenAvoidFoxFast = getDouble("mobs.chicken.avoid-fox.fast-speed", chickenAvoidFoxFast);
    }
}

