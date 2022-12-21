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

public class AddMinionLocationCommand implements ICataCommand {

    private final Dungeons plugin;

    public AddMinionLocationCommand(Dungeons plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "addminionlocation";
    }

    @Override
    public String getPermission() {
        return "dungeons.command.addminionlocation";
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
        return "/dungeons addminionlocation <Dungeon> <MythicMob>";
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {

        DungeonManager dungeonManager = plugin.getDungeonManager();

        if (!dungeonManager.containsDungeon(args[0])) {
            sender.sendMessage("Unknown dungeon");
            return;
        }

        Dungeon dungeon = dungeonManager.getDungeon(args[0]);

        if (!dungeon.containsMinion(args[1])) {
            sender.sendMessage("Unknown mob");
            return;
        }

        DungeonMob mob = dungeon.getMinion(args[1]);
        mob.getSpawnLocations().add(((Player) sender).getLocation());
        ColorUtils.sendMessage(sender, plugin.PREFIX + "Added minion location");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], plugin.getDungeonManager().dungeonNames(), new ArrayList<>());
        } else if (args.length == 2) {
            List<String> minionList = new ArrayList<>();
            Dungeons.inst().getDungeonManager().getDungeon(args[0]).getMinions().forEach(dungeonMob ->  minionList.add(dungeonMob.getName()));
            return StringUtil.copyPartialMatches(args[1], minionList, new ArrayList<>());
        }
        return Collections.emptyList();
    }
}
