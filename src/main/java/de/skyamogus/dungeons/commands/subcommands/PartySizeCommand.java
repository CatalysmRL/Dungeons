package de.skyamogus.dungeons.commands.subcommands;

import de.skyamogus.dungeons.Dungeons;
import de.skyamogus.dungeons.commands.components.CommandRange;
import de.skyamogus.dungeons.commands.components.ICataCommand;
import de.skyamogus.dungeons.dungeon.DungeonManager;
import de.skyamogus.dungeons.dungeon.components.Dungeon;
import de.skyamogus.dungeons.util.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PartySizeCommand implements ICataCommand {

    private final Dungeons plugin;

    public PartySizeCommand(Dungeons plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "partysize";
    }

    @Override
    public String getPermission() {
        return "dungeons.command.partysize";
    }

    @Override
    public boolean onlyPlayers() {
        return false;
    }

    @Override
    public CommandRange getRange() {
        return new CommandRange(2, 2);
    }

    @Override
    public String getUsage() {
        return "/dungeons partysize <dungeon>";
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {
        DungeonManager dungeonManager = plugin.getDungeonManager();

        if (!dungeonManager.containsDungeon(args[0])) {
            sender.sendMessage("Unknown dungeon");
            return;
        }

        int size;

        try {
            size = Integer.parseInt(args[1]);
        } catch (NumberFormatException exception) {
            sender.sendMessage("Invalid number input");
            return;
        }

        Dungeon dungeon = dungeonManager.getDungeon(args[0]);
        dungeon.setPartySize(size);

        ColorUtils.sendMessage(sender, plugin.PREFIX + "Set party size to &f" + args[1]);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], plugin.getDungeonManager().dungeonNames(), new ArrayList<>());
        }
        return Collections.emptyList();
    }
}
