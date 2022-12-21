package de.skyamogus.dungeons.commands.components;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public interface ICataCommand {

    String getName();

    default List<String> getAliases() {
        return new ArrayList<>();
    }

    String getPermission();

    boolean onlyPlayers();

    CommandRange getRange();

    String getUsage();

    void execute(CommandSender sender, Command command, String label, String[] args);

    List<String> tabComplete(CommandSender sender, Command command, String label, String[] args);
}
