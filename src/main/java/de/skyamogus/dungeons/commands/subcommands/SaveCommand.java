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

public class SaveCommand implements ICataCommand {

    private final Dungeons plugin;

    public SaveCommand(Dungeons plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "save";
    }

    @Override
    public String getPermission() {
        return "dungeons.command.save";
    }

    @Override
    public boolean onlyPlayers() {
        return false;
    }

    @Override
    public CommandRange getRange() {
        return new CommandRange(1, 1);
    }

    @Override
    public String getUsage() {
        return "/dungeons save <Dungeon>";
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {
        DungeonManager dungeonManager = plugin.getDungeonManager();

        if (!dungeonManager.containsDungeon(args[0])) {
            sender.sendMessage("Unkown dungeon");
            return;
        }

        Dungeon dungeon = dungeonManager.getDungeon(args[0]);
        dungeon.saveToDisk();

        ColorUtils.sendMessage(sender, plugin.PREFIX + "&aSaved dungeon");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], plugin.getDungeonManager().dungeonNames(), new ArrayList<>());
        }
        return Collections.emptyList();
    }
}
