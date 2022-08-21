// Original code by MrFishCakes, licensed under (license unknown)
// You can find the original code at (link taken down)

package com.notsatvrn.graphene.commands;

import com.notsatvrn.graphene.util.GrapheneUtil;
import com.notsatvrn.graphene.config.GrapheneConfig;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import javax.swing.Timer;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class to manage the <i>/stop</i> command
 *
 * @author MrFishCakes
 */
public class StopCommand extends Command {
    private static final Map<String, TimeUnit> VALID_UNITS = new HashMap<>() {{
        put("sec", TimeUnit.SECONDS);
        put("secs", TimeUnit.SECONDS);
        put("seconds", TimeUnit.SECONDS);
        put("min", TimeUnit.MINUTES);
        put("mins", TimeUnit.MINUTES);
        put("minutes", TimeUnit.MINUTES);
        put("hr", TimeUnit.HOURS);
        put("hrs", TimeUnit.HOURS);
        put("hours", TimeUnit.HOURS);
    }};

    private static Timer stopTimer;

    private final MiniMessage message;

    public StopCommand() {
        super("stop");
        this.description = "Enhanced stop command";
        this.usageMessage = "/stop [time | cancel] [unit]";
        this.setPermission("bukkit.command.stop");
        this.message = MiniMessage.miniMessage();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!testPermission(sender)) return true;

        if (args.length == 1 || args.length == 2) {
            if (args[0].equalsIgnoreCase("cancel")) {
                if (stopTimer == null) {
                    GrapheneUtil.sendMessage(message, sender, GrapheneConfig.stopCommandNotScheduled);
                    return true;
                }

                try {
                    stopTimer.stop();
                } catch (Exception ex) { // Just in case
                    GrapheneUtil.sendMessage(message, sender, GrapheneConfig.stopCommandCancelFail);
                    return true;
                }

                GrapheneUtil.sendMessage(message, sender, GrapheneConfig.stopCommandCancelled);
                stopTimer = null;
                return true;
            }

            if (stopTimer != null) {
                GrapheneUtil.sendMessage(message, sender, GrapheneConfig.stopCommandAlreadyScheduled);
                return true;
            }

            int time;
            TimeUnit unit = TimeUnit.SECONDS;

            try {
                time = Integer.parseInt(args[0]);
            } catch (NumberFormatException ex) {
                return sendUsageMessage(sender);
            }

            if (time < 1) return sendUsageMessage(sender);

            if (args.length == 2) {
                unit = VALID_UNITS.get(args[1].toLowerCase());

                if (unit == null) unit = TimeUnit.SECONDS;
            }

            stopTimer = new Timer((int) unit.toMillis(time), e -> Bukkit.shutdown());

            stopTimer.setRepeats(false);
            stopTimer.start();

            GrapheneUtil.sendMessage(message, sender, GrapheneConfig.stopCommandSuccess);
            return true;
        } else if (args.length == 0) {
            Bukkit.shutdown();
            return true;
        } else {
            return sendUsageMessage(sender);
        }
    }

    @Override
    @NotNull
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (!testPermissionSilent(sender)) return Collections.emptyList();

        if (args.length == 1) {
            if (stopTimer == null) return Collections.singletonList("[time]");

            return Stream.of("cancel").filter(arg -> arg.startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        }

        if (args.length == 2) {
            return VALID_UNITS.keySet().stream().filter(arg -> arg.startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private boolean sendUsageMessage(CommandSender sender) {
        GrapheneUtil.sendMessage(sender, ChatColor.RED + "Usage: " + usageMessage);
        return true;
    }
}

