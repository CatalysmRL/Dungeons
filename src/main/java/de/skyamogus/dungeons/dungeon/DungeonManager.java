package de.skyamogus.dungeons.dungeon;

import de.skyamogus.dungeons.Dungeons;
import de.skyamogus.dungeons.dungeon.components.Dungeon;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DungeonManager {

    private final Dungeons plugin;

    public DungeonManager(Dungeons plugin) {
        this.plugin = plugin;
        loadDungeons();
    }

    public List<Dungeon> dungeons = new ArrayList<>();

    public void loadDungeons() {
        File folder = new File(plugin.getDataFolder() + "/dungeons");
        if (!folder.exists()) return;

        dungeons.clear();

        for (File value : folder.listFiles(File::isFile)) {
            YamlConfiguration valueCfg = YamlConfiguration.loadConfiguration(value);
            if (valueCfg.contains("Dungeon") && valueCfg.get("Dungeon") != null) {
                Dungeon dungeon = (Dungeon) valueCfg.get("Dungeon");
                dungeons.add(dungeon);
            }
        }

        for (Dungeon dungeon : dungeons) {
            dungeon.saveToDisk();
        }
    }

    public void unloadDungeons() {
        for (Dungeon dungeon : dungeons) {
            dungeon.getDungeonVillager().removeVillager();
        }

        dungeons.clear();
    }

    public Dungeon getDungeon(String name) {
        for (Dungeon dungeon : dungeons) {
            if (dungeon.getName().equals(name)) return dungeon;
        }

        return null;
    }

    public boolean containsDungeon(String name) {
        for (Dungeon dungeon : dungeons) {
            if (dungeon.getName().equals(name)) return true;
        }
        return false;
    }

    public void registerDungeon(Dungeon dungeon) {
        if (containsDungeon(dungeon.getName())) return;
        dungeons.add(dungeon);
    }

    public List<String> dungeonNames() {
        return dungeons.stream().map(Dungeon::getName).collect(Collectors.toList());
    }
}
