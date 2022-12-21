package de.skyamogus.dungeons.commands.subcommands;

import de.skyamogus.dungeons.Dungeons;
import de.skyamogus.dungeons.commands.components.CommandRange;
import de.skyamogus.dungeons.commands.components.ICataCommand;
import de.skyamogus.dungeons.dungeon.DungeonManager;
import de.skyamogus.dungeons.util.ColorUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class ListCommand implements ICataCommand {

    private final Dungeons plugin;

    public ListCommand(Dungeons plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getPermission() {
        return "dungeons.command.list";
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
        return "/dungeons list";
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {
        DungeonManager dungeonManager = plugin.getDungeonManager();

        StringBuilder builder = new StringBuilder("All registered dungeons:\n&f");
        dungeonManager.dungeonNames().forEach(s -> builder.append("&f").append(s).append("&7, "));

        ColorUtils.sendMessage(sender, plugin.PREFIX + StringUtils.chop(StringUtils.strip(builder.toString())));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
