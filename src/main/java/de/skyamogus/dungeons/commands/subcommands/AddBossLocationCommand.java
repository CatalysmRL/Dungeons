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
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddBossLocationCommand implements ICataCommand {

    private final Dungeons plugin;

    public AddBossLocationCommand(Dungeons plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "addbosslocation";
    }

    @Override
    public String getPermission() {
        return "dungeons.command.addbosslocation";
    }

    @Override
    public boolean onlyPlayers() {
        return true;
    }

    @Override
    public CommandRange getRange() {
        return new CommandRange(2, 2);
    }

    @Override
    public String getUsage() {
        return "/dungeons addbosslocation <Dungeon> <MythicMob>";
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {

        DungeonManager dungeonManager = plugin.getDungeonManager();

        if (!dungeonManager.containsDungeon(args[0])) {
            sender.sendMessage("Unknown dungeon");
            return;
        }

        Dungeon dungeon = dungeonManager.getDungeon(args[0]);

        if (!dungeon.containsBoss(args[1])) {
            sender.sendMessage("Unknown mob");
            return;
        }

        DungeonMob mob = dungeon.getBoss(args[1]);
        mob.getSpawnLocations().add(((Player) sender).getLocation());
        ColorUtils.sendMessage(sender, plugin.PREFIX + "Added boss location");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], plugin.getDungeonManager().dungeonNames(), new ArrayList<>());
        } else if (args.length == 2) {
            List<String> bossesList = new ArrayList<>();
            Dungeons.inst().getDungeonManager().getDungeon(args[0]).getBosses().forEach(dungeonMob ->  bossesList.add(dungeonMob.getName()));
            return StringUtil.copyPartialMatches(args[1], bossesList, new ArrayList<>());
        }
        return Collections.emptyList();
    }

}
