package de.skyamogus.dungeons.commands.subcommands;

import de.skyamogus.dungeons.Dungeons;
import de.skyamogus.dungeons.commands.components.CommandRange;
import de.skyamogus.dungeons.commands.components.ICataCommand;
import de.skyamogus.dungeons.dungeon.DungeonManager;
import de.skyamogus.dungeons.dungeon.components.Dungeon;
import de.skyamogus.dungeons.util.ColorUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class CreateCommand implements ICataCommand {

    private final Dungeons plugin;

    public CreateCommand(Dungeons plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getPermission() {
        return "dungeons.command.create";
    }

    @Override
    public boolean onlyPlayers() {
        return true;
    }

    @Override
    public CommandRange getRange() {
        return new CommandRange(1, 1);
    }

    @Override
    public String getUsage() {
        return "/dungeons create <dungeon>";
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        DungeonManager dungeonManager = plugin.getDungeonManager();

        if (dungeonManager.containsDungeon(args[0])) {
            player.sendMessage("Dungeon already exists.");
            return;
        }

        Location villagerLocation = player.getTargetBlock(5).getLocation().add(0.5, 1, 0.5);
        villagerLocation.setDirection(player.getLocation().getDirection().multiply(-1));
        villagerLocation.setPitch(0f);
        Dungeon dungeon = new Dungeon(args[0], villagerLocation);
        dungeon.getDungeonVillager().spawnVillager(villagerLocation);
        dungeonManager.registerDungeon(dungeon);

        ColorUtils.sendMessage(sender, plugin.PREFIX + "Successfully created dungeon &f" + args[0]);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
