// Original code by SugarcaneMC, licensed under GNU General Public License v3.0
// You can find the original code on https://github.com/SugarcaneMC/Sugarcane/tree/1.18.2/dev

// Original code by MrFishCakes, licensed under (license unknown)
// You can find the original code at (link taken down)

package com.notsatvrn.graphene.util;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class GrapheneUtil {
    private final static int ConsoleBarWidth = 78; // 80 column display

    public static int getIndentation(String s) {
        if (!s.startsWith(" ")) return 0;
        int i = 0;
        while ((s = s.replaceFirst(" ", "")).startsWith(" ")) i++;
        return i+1;
    }

    public static void logDebug(String s) {
        if (com.notsatvrn.graphene.config.GrapheneConfig.debug) System.out.println(s);
    }

    public static String getTextProgressBar(double value) {
        int progress = (int) (value*ConsoleBarWidth);
        return String.format("[%s%s]", "=".repeat(progress), "_".repeat(ConsoleBarWidth - progress));
    }

    public static void sendMessage(@NotNull CommandSender sender, @NotNull String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replaceAll("''", "'")));
    }

    public static void sendMessage(@NotNull MiniMessage miniMessage, @NotNull CommandSender sender, @NotNull String message) {
        sender.sendMessage(miniMessage.parse(message.replaceAll("''", "'")));
    }
}

