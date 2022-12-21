package de.skyamogus.dungeons.commands.subcommands;

import de.skyamogus.dungeons.Dungeons;
import de.skyamogus.dungeons.commands.components.CommandRange;
import de.skyamogus.dungeons.commands.components.ICataCommand;
import de.skyamogus.dungeons.dungeon.DungeonManager;
import de.skyamogus.dungeons.dungeon.components.Dungeon;
import de.skyamogus.dungeons.util.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeleteCommand implements ICataCommand {

    private final Dungeons plugin;

    public DeleteCommand(Dungeons plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public String getPermission() {
        return "dungeons.command.delete";
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
        return "/dungeons delete <dungeon>";
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
        dungeon.getDungeonVillager().removeVillager();
        dungeonManager.dungeons.remove(dungeon);
        File file = new File(plugin.getDataFolder() + "/dungeons/" + dungeon.getName() + ".yml");
        if (file.exists()) {
            file.delete();
        }

        ColorUtils.sendMessage(sender, "Deleted dungeon &f" + args[0]);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], plugin.getDungeonManager().dungeonNames(), new ArrayList<>());
        }
        return Collections.emptyList();
    }
}
