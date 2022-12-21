package de.skyamogus.dungeons.dungeon.components.dungeoncomp;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DungeonMob {

    private String name;
    private List<Location> spawnLocations = new ArrayList<>();

    public DungeonMob(String name) {
        this.name = name;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();

        map.put("name", name);
        map.put("locations", spawnLocations);

        return map;
    }

    public static DungeonMob deserialize(Map<String, Object> serializedMob) {

        String name = (String) serializedMob.get("name");
        ArrayList<Location> locations = (ArrayList<Location>) serializedMob.get("locations");

        DungeonMob dungeonMob = new DungeonMob(name);
        dungeonMob.setSpawnLocations(locations);

        return dungeonMob;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Location> getSpawnLocations() {
        return spawnLocations;
    }

    public void setSpawnLocations(List<Location> spawnLocations) {
        this.spawnLocations = spawnLocations;
    }
}