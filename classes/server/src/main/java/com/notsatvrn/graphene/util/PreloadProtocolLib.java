// Original code by SugarcaneMC, licensed under GNU General Public License v3.0
// You can find the original code on https://github.com/SugarcaneMC/Sugarcane/tree/1.18.2/dev

package com.notsatvrn.graphene.util;

import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Method;

public class PreloadProtocolLib {
    public synchronized static void run() {
        try {
            final SimplePluginManager pluginManager = (SimplePluginManager) Bukkit.getPluginManager();
            final Plugin protocolLib = pluginManager.getPlugin("ProtocolLib");
            if(protocolLib != null && protocolLib.isEnabled()) {
                MinecraftServer.LOGGER.info("Graphene: Attempting to preload ProtocolLib's EnumWrappers...");
                final Method initialize = Class.forName("com.comphenix.protocol.wrappers.EnumWrappers", true, protocolLib.getClass().getClassLoader()).getDeclaredMethod("initialize");
                initialize.setAccessible(true);
                initialize.invoke(null);
                synchronized (PreloadProtocolLib.class) {
                }
            }
        } catch (Throwable t) {
            MinecraftServer.LOGGER.warn("Graphene: Failed to preload ProtocolLib's EnumWrappers.", t);
        }
    }
}

