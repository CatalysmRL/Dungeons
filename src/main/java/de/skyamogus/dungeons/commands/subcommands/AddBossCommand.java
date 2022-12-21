package de.skyamogus.dungeons.commands.subcommands;

import de.skyamogus.dungeons.Dungeons;
import de.skyamogus.dungeons.commands.components.CommandRange;
import de.skyamogus.dungeons.commands.components.ICataCommand;
import de.skyamogus.dungeons.dungeon.DungeonManager;
import de.skyamogus.dungeons.dungeon.components.Dungeon;
import de.skyamogus.dungeons.dungeon.components.dungeoncomp.DungeonMob;
import de.skyamogus.dungeons.util.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddBossCommand implements ICataCommand {

    private final Dungeons plugin;

    public AddBossCommand(Dungeons plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "addboss";
    }

    @Override
    public String getPermission() {
        return "dungeons.command.addboss";
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
        return "/dungeons addboss <Dungeon> <MythicMob>";
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {

        DungeonManager dungeonManager = plugin.getDungeonManager();

        if (!dungeonManager.containsDungeon(args[0])) {
            sender.sendMessage("Unknown dungeon");
            return;
        }

        Dungeon dungeon = dungeonManager.getDungeon(args[0]);
        dungeon.getBosses().add(new DungeonMob(args[1]));

        ColorUtils.sendMessage(sender, plugin.PREFIX + "Added boss &f" + args[1]);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], plugin.getDungeonManager().dungeonNames(), new ArrayList<>());
        }
        return Collections.emptyList();
    }
}
