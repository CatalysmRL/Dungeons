package de.skyamogus.dungeons.commands.subcommands;

import de.skyamogus.dungeons.Dungeons;
import de.skyamogus.dungeons.commands.components.CommandRange;
import de.skyamogus.dungeons.commands.components.ICataCommand;
import de.skyamogus.dungeons.util.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class ReloadCommand implements ICataCommand {

    private final Dungeons plugin;

    public ReloadCommand(Dungeons plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getPermission() {
        return "dungeons.command.reload";
    }

    @Override
    public boolean onlyPlayers() {
        return false;
    }

    @Override
    public CommandRange getRange() {
        return new CommandRange(0, 0);
    }

    @Override
    public String getUsage() {
        return "/dungeons reload";
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {
        plugin.getDungeonManager().unloadDungeons();
        plugin.getDungeonManager().loadDungeons();

        ColorUtils.sendMessage(sender, plugin.PREFIX + "&aReloaded dungeons");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
