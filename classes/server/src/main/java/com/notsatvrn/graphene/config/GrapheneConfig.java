package com.notsatvrn.graphene.config;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import com.notsatvrn.graphene.commands.GrapheneCommand;
import com.notsatvrn.graphene.commands.NSPTCommand;
import com.notsatvrn.graphene.commands.StatsCommand;
import com.notsatvrn.graphene.commands.StopCommand;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@SuppressWarnings("unused")
public class GrapheneConfig {
    private static final String HEADER = "This is the main configuration file for Graphene.\n"
            + "As you can see, there's tons to configure. Some options may impact gameplay, so use\n"
            + "with caution, and make sure you know what each option does before configuring.\n";
    private static File CONFIG_FILE;
    public static YamlConfiguration config;

    private static Map<String, Command> commands;

    public static int version;
    static boolean verbose;

    public static void init(File configFile) {
        CONFIG_FILE = configFile;
        config = new YamlConfiguration();
        try {
            config.load(CONFIG_FILE);
        } catch (IOException ignore) {
        } catch (InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not load graphene.yml, please correct your syntax errors", ex);
            throw Throwables.propagate(ex);
        }
        config.options().header(HEADER);
        config.options().copyDefaults(true);
        verbose = getBoolean("verbose", false);

        commands = new HashMap<>();
        commands.put("graphene", new GrapheneCommand());
        commands.put("nspt", new NSPTCommand());
        commands.put("stats", new StatsCommand());

        version = getInt("config-version", 1);
        set("config-version", 1);

        readConfig(GrapheneConfig.class, null);

        if (stopCommandEnabled) {
            MinecraftServer.getServer().vanillaCommandDispatcher.getDispatcher().getRoot().removeCommand("stop"); // This unregisters minecraft:stop
            commands.put("stop", new StopCommand());
        }
    }

    protected static void log(String s) {
        if (verbose) {
            log(Level.INFO, s);
        }
    }

    protected static void log(Level level, String s) {
        Bukkit.getLogger().log(level, s);
    }

    public static void registerCommands() {
        for (Map.Entry<String, Command> entry : commands.entrySet()) {
            MinecraftServer.getServer().server.getCommandMap().register(entry.getKey(), "Graphene", entry.getValue());
        }
    }

    static void readConfig(Class<?> clazz, Object instance) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (Modifier.isPrivate(method.getModifiers())) {
                if (method.getParameterTypes().length == 0 && method.getReturnType() == Void.TYPE) {
                    try {
                        method.setAccessible(true);
                        method.invoke(instance);
                    } catch (InvocationTargetException ex) {
                        throw Throwables.propagate(ex.getCause());
                    } catch (Exception ex) {
                        Bukkit.getLogger().log(Level.SEVERE, "Error invoking " + method, ex);
                    }
                }
            }
        }

        try {
            config.save(CONFIG_FILE);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save " + CONFIG_FILE, ex);
        }
    }

    private static void set(String path, Object val) {
        config.addDefault(path, val);
        config.set(path, val);
    }

    private static String getString(String path, String def) {
        config.addDefault(path, def);
        return config.getString(path, config.getString(path));
    }

    private static boolean getBoolean(String path, boolean def) {
        config.addDefault(path, def);
        return config.getBoolean(path, config.getBoolean(path));
    }

    private static double getDouble(String path, double def) {
        config.addDefault(path, def);
        return config.getDouble(path, config.getDouble(path));
    }

    private static int getInt(String path, int def) {
        config.addDefault(path, def);
        return config.getInt(path, config.getInt(path));
    }

    private static <T> List getList(String path, T def) {
        config.addDefault(path, def);
        return config.getList(path, config.getList(path));
    }

    static Map<String, Object> getMap(String path, Map<String, Object> def) {
        if (def != null && config.getConfigurationSection(path) == null) {
            config.addDefault(path, def);
            return def;
        }
        return toMap(config.getConfigurationSection(path));
    }

    private static Map<String, Object> toMap(ConfigurationSection section) {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        if (section != null) {
            for (String key : section.getKeys(false)) {
                Object obj = section.get(key);
                if (obj != null) {
                    builder.put(key, obj instanceof ConfigurationSection val ? toMap(val) : obj);
                }
            }
        }
        return builder.build();
    }

    // Debug - Sugarcane
    public static boolean debug = false;
    private static void debug() {
        debug = getBoolean("settings.debug", debug);
    }

    // Fix ProtocolLib - Sugarcane
    public static boolean fixProtocolLib = true;
    private static void fixProtocolLib() {
        fixProtocolLib = getBoolean("settings.fix-protocol-lib", fixProtocolLib);
    }

    // Checks - Yatopia
    public static boolean checkFlying = true;
    public static boolean checkVehicleFlying = true;
    public static boolean checkFastSpeed = true;
    private static void checks() {
        checkFlying = getBoolean("settings.checks.flight", checkFlying);
        checkVehicleFlying = getBoolean("settings.checks.vehicle-flight", checkVehicleFlying);
        checkFastSpeed = getBoolean("settings.checks.fast-speed", checkFastSpeed);
    }

    // Map Update Interval - Mirai
    public static int mapUpdateInterval = 5;
    private static void mapUpdateInterval() {
        mapUpdateInterval = getInt("settings.map-update-interval", mapUpdateInterval);
    }

    // Player Login Location Logging - JettPack
    public static boolean logPlayerLoginLoc = true;
    private static void logPlayerLoginLoc() {
        logPlayerLoginLoc = getBoolean("settings.log-player-login-location", logPlayerLoginLoc);
    }

    // Hide Joining Player IPs - Nyper
    public static boolean hidePlayerIPs = false;
    private static void hidePlayerIPs() {
        hidePlayerIPs = getBoolean("settings.hide-player-ips", hidePlayerIPs);
    }

    // Stop Command - Crunchy
    public static boolean stopCommandEnabled = false;
    public static String stopCommandNotScheduled = "<red>There is no server stop scheduled, use ''/stop [time] [unit]''";
    public static String stopCommandCancelFail = "<red>An error occurred when trying to deactivate the timer. The stop MIGHT still occur!";
    public static String stopCommandCancelled = "<green>The server stop has been cancelled!";
    public static String stopCommandAlreadyScheduled = "<red>A server stop is already scheduled, to cancel it use ''/stop cancel''";
    public static String stopCommandSuccess = "<green>The stop has been successfully scheduled!";
    private static void stopCommandSettings() {
        stopCommandEnabled = getBoolean("commands.stop.enabled", stopCommandEnabled);
        stopCommandNotScheduled = getString("commands.stop.not-scheduled", stopCommandNotScheduled);
        stopCommandCancelFail = getString("commands.stop.cancel-fail", stopCommandCancelFail);
        stopCommandCancelled = getString("commands.stop.cancelled", stopCommandCancelled);
        stopCommandAlreadyScheduled = getString("commands.stop.already-scheduled", stopCommandAlreadyScheduled);
        stopCommandSuccess = getString("commands.stop.success", stopCommandSuccess);
    }


    // Smelt Raw Blocks - Crunchy
    public static RawBlockData smeltRawCopperBlock;
    public static RawBlockData smeltRawGoldBlock;
    public static RawBlockData smeltRawIronBlock;
    private static void smeltRawBlocks() {
        final String copperRoot = "recipes.smelting.raw-copper-block.";
        final String goldRoot = "recipes.smelting.raw-gold-block.";
        final String ironRoot = "recipes.smelting.raw-iron-block.";

        if (version < 2) {
            set(copperRoot + "enabled", getBoolean("recipes.smelt-raw-copper-block", true));
            set("recipes.smelt-raw-copper-block", null);

            set(goldRoot + "enabled", getBoolean("recipes.smelt-raw-gold-block", true));
            set("recipes.smelt-raw-gold-block", null);

            set(ironRoot + "enabled", getBoolean("recipes.smelt-raw-iron-block", true));
            set("recipes.smelt-raw-iron-block", null);
        } else {
            smeltRawCopperBlock = RawBlockData.fromValues(getBoolean(copperRoot + "enabled", true), (float) getDouble(copperRoot + "experience", 6.3D),
                    getInt(copperRoot + "cook-time", 800), getInt(copperRoot + "blast-cook-time", 500));
            smeltRawGoldBlock = RawBlockData.fromValues(getBoolean(goldRoot + "enabled", true), (float) getDouble(goldRoot + "experience", 9.0D),
                    getInt(goldRoot + "cook-time", 800), getInt(goldRoot + "blast-cook-time", 500));
            smeltRawIronBlock = RawBlockData.fromValues(getBoolean(ironRoot + "enabled", true), (float) getDouble(ironRoot + "experience", 6.3D),
                    getInt(ironRoot + "cook-time", 800), getInt(ironRoot + "blast-cook-time", 500));
        }
    }

    public static class RawBlockData {
        private final boolean enabled;
        private final float experience;
        private final int cookTime;
        private final int blastCookTime;

        private RawBlockData(boolean enabled, float experience, int cookTime, int blastCookTime) {
            this.enabled = enabled;
            this.experience = experience > 0 ? experience : 5F;
            this.cookTime = cookTime > 0 ? cookTime : 40;
            this.blastCookTime = blastCookTime > 0 ? blastCookTime : 20;
        }

        @NotNull
        private static RawBlockData fromValues(boolean enabled, float experience, int cookTime, int blastCookTime) {
            if (experience < 1) experience = 1F;
            if (cookTime < 1) cookTime = 1;
            if (blastCookTime < 1) blastCookTime = 1;

            return new RawBlockData(enabled, experience, cookTime,blastCookTime);
        }

        public boolean enabled() {
            return enabled;
        }

        public float experience() {
            return experience;
        }

        public int cookTime() {
            return cookTime;
        }

        public int blastCookTime() {
            return blastCookTime;
        }
    }
}

