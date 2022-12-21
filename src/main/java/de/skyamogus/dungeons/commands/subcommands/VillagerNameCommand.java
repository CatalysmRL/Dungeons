package de.skyamogus.dungeons.commands.subcommands;

import de.skyamogus.dungeons.Dungeons;
import de.skyamogus.dungeons.commands.components.CommandRange;
import de.skyamogus.dungeons.commands.components.ICataCommand;
import de.skyamogus.dungeons.dungeon.DungeonManager;
import de.skyamogus.dungeons.dungeon.components.Dungeon;
import de.skyamogus.dungeons.dungeon.components.DungeonVillager;
import de.skyamogus.dungeons.util.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VillagerNameCommand implements ICataCommand {

    private final Dungeons plugin;

    public VillagerNameCommand(Dungeons plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "villagername";
    }

    @Override
    public String getPermission() {
        return "dungeons.command.villagername";
    }

    @Override
    public boolean onlyPlayers() {
        return true;
    }

    @Override
    public CommandRange getRange() {
        return new CommandRange(2, -1);
    }

    @Override
    public String getUsage() {
        return "/dungeons villagername <dungeon> <name>";
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        DungeonManager dungeonManager = plugin.getDungeonManager();

        if (!dungeonManager.containsDungeon(args[0])) {
            player.sendMessage("Unknown dungeon");
            return;
        }

        Dungeon dungeon = dungeonManager.getDungeon(args[0]);
        DungeonVillager dungeonVillager = dungeon.getDungeonVillager();

        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            builder.append(args[i]).append(" ");
        }

        String customName = builder.deleteCharAt(builder.length() - 1).toString();
        dungeonVillager.setCustomName(customName);

        ColorUtils.sendMessage(player, plugin.PREFIX + "Set villager name to " + args[1]);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], plugin.getDungeonManager().dungeonNames(), new ArrayList<>());
        } else if (args.length == 2) {
            return StringUtil.copyPartialMatches(args[1], Collections.singletonList(plugin.getDungeonManager().getDungeon(args[0]).getDungeonVillager().getCustomName()), new ArrayList<>());
        }
        return Collections.emptyList();
    }

}
