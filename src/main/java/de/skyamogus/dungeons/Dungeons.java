package de.skyamogus.dungeons;

import de.skyamogus.dungeons.commands.components.CommandHandler;
import de.skyamogus.dungeons.commands.subcommands.*;
import de.skyamogus.dungeons.dungeon.DungeonManager;
import de.skyamogus.dungeons.dungeon.components.Dungeon;
import de.skyamogus.dungeons.listeners.DungeonListener;
import de.skyamogus.dungeons.listeners.VillagerListeners;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Dungeons extends JavaPlugin {

    private static Dungeons plugin;
    public static Dungeons inst() {
        return plugin;
    }
    private DungeonManager dungeonManager;

    public String PREFIX = "&6[&eDungeons&6]: &7";

    @Override
    public void onEnable() {

        plugin = this;
        ConfigurationSerialization.registerClass(Dungeon.class);

        PluginManager pm = Bukkit.getPluginManager();

        if (
                pm.getPlugin("Parties") == null ||
                        !pm.getPlugin("Parties").isEnabled() ||
                        pm.getPlugin("MythicMobs") == null ||
                        !pm.getPlugin("MythicMobs").isEnabled()) {
            pm.disablePlugin(this);
        }

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        dungeonManager = new DungeonManager(this);

        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        dungeonManager.unloadDungeons();
    }

    public void registerCommands() {
        CommandHandler commandHandler = new CommandHandler();
        commandHandler.registerCommand(new CreateCommand(this));
        commandHandler.registerCommand(new VillagerKeyCommand(this));
        commandHandler.registerCommand(new DungeonSpawnCommand(this));
        commandHandler.registerCommand(new ListCommand(this));
        commandHandler.registerCommand(new PartySizeCommand(this));
        commandHandler.registerCommand(new AddMinionCommand(this));
        commandHandler.registerCommand(new AddBossCommand(this));
        commandHandler.registerCommand(new AddMinionLocationCommand(this));
        commandHandler.registerCommand(new AddBossLocationCommand(this));
        commandHandler.registerCommand(new DeleteCommand(this));
        commandHandler.registerCommand(new VillagerNameCommand(this));
        commandHandler.registerCommand(new SaveCommand(this));
        commandHandler.registerCommand(new ReloadCommand(this));

        getCommand("dungeons").setExecutor(commandHandler);
    }

    public void registerListeners() {
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new VillagerListeners(this), this);
        pm.registerEvents(new DungeonListener(this), this);
    }

    public DungeonManager getDungeonManager() {
        return dungeonManager;
    }
}
