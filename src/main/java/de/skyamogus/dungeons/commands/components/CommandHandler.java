package de.skyamogus.dungeons.commands.components;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CommandHandler implements TabExecutor {
    private final Map<String, ICataCommand> subCommands = new HashMap<>();

    public void registerCommand(ICataCommand command) {
        subCommands.put(command.getName(), command);
    }

    public boolean containsCommand(String subCmd) {
        if (subCommands.containsKey(subCmd)) return true;
        for (ICataCommand cmd : subCommands.values()) {
            if (cmd.getAliases().contains(subCmd)) return true;
        }
        return false;
    }

    public ICataCommand getCommand(String subCmd) {
        if (subCommands.containsKey(subCmd)) {
            return subCommands.get(subCmd);
        }
        for (ICataCommand cmd : subCommands.values()) {
            if (cmd.getAliases().contains(subCmd)) return cmd;
        }
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 0) {
            StringBuilder builder = new StringBuilder("These are the available commands for dungeons:");
            for (ICataCommand cmd : subCommands.values()) {
                builder.append("\n").append("/").append(cmd.getName());
            }
            sender.sendMessage(builder.toString());
            return true;
        }

        String subCmd = args[0].toLowerCase();
        if (!containsCommand(subCmd)) {
            sender.sendMessage("Unknown command");
            return true;
        }

        ICataCommand subCommand = getCommand(subCmd);
        if (!sender.hasPermission(subCommand.getPermission())) {
            sender.sendMessage("You don't have the permissions for this command");
            return true;
        }

        if (subCommand.onlyPlayers() && !(sender instanceof Player)) {
            sender.sendMessage("Only players may execute this command");
            return true;
        }

        String[] strippedArgs = Arrays.copyOfRange(args, 1, args.length);

        if (!subCommand.getRange().inRange(strippedArgs.length)) {
            sender.sendMessage(subCommand.getUsage());
            return true;
        }

        subCommand.execute(sender, command, label, strippedArgs);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], new ArrayList<>(subCommands.keySet()), new ArrayList<>());
        } else {
            if (!containsCommand(args[0].toLowerCase())) return Collections.emptyList();
            return getCommand(args[0]).tabComplete(sender, command, alias, Arrays.copyOfRange(args, 1, args.length));
        }
    }


}
