package com.notsatvrn.graphene.commands;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.notsatvrn.graphene.config.GrapheneConfig;
import com.notsatvrn.graphene.commands.StopCommand;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GrapheneCommand extends Command {
    public GrapheneCommand() {
        super("graphene");
        this.description = "Graphene related commands";
        this.usageMessage = "/graphene [reload | version]";
        this.setPermission("graphene.command.graphene");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location) throws IllegalArgumentException {
        if (args.length == 1) {
            return Stream.of("reload", "version")
                    .filter(arg -> arg.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        switch (args[0].toLowerCase(i)) {
            case "reload":
                reloadGraphene(sender);
                break;
            case "version":
                Command verCmd = org.bukkit.Bukkit.getServer().getCommandMap().getCommand("version");
                if (verCmd != null) {
                    return verCmd.execute(sender, commandLabel, com.notsatvrn.graphene.util.Constants.EMPTY_string_arr);
                };
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                return false;
        }

        return true;
    }

    public void reloadGraphene(CommandSender sender) {
        Command.broadcastCommandMessage(sender, ChatColor.RED + "Please note that this command is not supported and may cause issues.");
        Command.broadcastCommandMessage(sender, ChatColor.RED + "If you encounter any issues please use the /stop command to restart your server.");

        MinecraftServer console = MinecraftServer.getServer();
        GrapheneConfig.init((File) console.options.valueOf("graphene-settings"));
        for (ServerLevel level : console.getAllLevels()) {
            level.grapheneConfig.init();
            level.resetBreedingCooldowns();
        }
        console.server.reloadCount++;

        Command.broadcastCommandMessage(sender, ChatColor.GREEN + "Graphene config reload complete.");
    }
}

