package de.skyamogus.dungeons.dungeon.components;

import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import de.skyamogus.dungeons.Dungeons;
import de.skyamogus.dungeons.dungeon.components.dungeoncomp.DungeonMob;
import de.skyamogus.dungeons.dungeon.components.dungeoncomp.DungeonState;
import de.skyamogus.dungeons.util.ConfigFile;
import io.lumine.mythic.api.exceptions.InvalidMobTypeException;
import io.lumine.mythic.bukkit.BukkitAPIHelper;
import io.lumine.mythic.bukkit.MythicBukkit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

public class Dungeon implements ConfigurationSerializable {

    private String name;
    private DungeonVillager dungeonVillager;
    private Location dungeonSpawn;
    private int partySize = 4;
    private final List<DungeonMob> minions = new ArrayList<>();
    private final List<DungeonMob> bosses = new ArrayList<>();

    private Party party;
    private final List<UUID> dungeonPlayers = new ArrayList<>();
    private final List<UUID> aliveMobs = new ArrayList<>();
    private final List<UUID> aliveBosses = new ArrayList<>();
    private DungeonState state = DungeonState.OPEN;


    private ConfigFile configFile;


    public Dungeon(String name) {
        this.name = name;
    }

    public Dungeon(String name, Location location) {
        this(name);
        this.dungeonVillager = new DungeonVillager(this);
    }

    public void start() {
        state = DungeonState.MINION;

        dungeonPlayers.clear();
        dungeonPlayers.addAll(party.getOnlineMembers().stream().map(PartyPlayer::getPlayerUUID).toList());

        new BukkitRunnable() {

            int countdown = 5;

            @Override
            public void run() {
                Component mainTitle = Component.text(countdown).color(TextColor.color(0, 255, 0));
                Component subTitle = Component.text("Entering the dungeon...").color(TextColor.color(255, 255, 0));

                Title title = Title.title(mainTitle, subTitle);

                for (Player player : getPlayers()) {
                    switch (countdown) {
                        case 0:
                            player.showTitle(Title.title(Component.text(""), Component.text("")));
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1F, 2F);
                            break;
                        case 3, 2, 1:
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1F, 1F);
                        default:
                            player.showTitle(title);
                    }
                }

                if (countdown <= 0) {
                    Bukkit.getScheduler().runTaskLater(Dungeons.inst(), () -> {
                        teleportPlayers();
                        spawnMobs();
                    }, 2L);
                    cancel();
                }

                countdown--;
            }
        }.runTaskTimer(Dungeons.inst(), 0L, 20L);
    }

    public void teleportPlayers() {
        for (UUID uuid : dungeonPlayers) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                party.broadcastMessage("Error while entering the dungeon", null);
                return;
            }
            player.teleportAsync(dungeonSpawn);
        }
    }

    public void spawnMobs() {
        BukkitAPIHelper mythicmobs = MythicBukkit.inst().getAPIHelper();

        for (UUID alivemob : aliveMobs) {
            if (Bukkit.getEntity(alivemob) != null) {
                Bukkit.getEntity(alivemob).remove();
            }
        }

        aliveMobs.clear();

        for (DungeonMob dungeonMob : minions) {
            for (Location location : dungeonMob.getSpawnLocations()) {
                try {
                    Entity entity = mythicmobs.spawnMythicMob(dungeonMob.getName(), location);
                    aliveMobs.add(entity.getUniqueId());
                } catch (InvalidMobTypeException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void spawnBosses() {
        BukkitAPIHelper mythicmobs = MythicBukkit.inst().getAPIHelper();

        for (UUID aliveBoss : aliveBosses) {
            if (Bukkit.getEntity(aliveBoss) != null) {
                Bukkit.getEntity(aliveBoss).remove();
            }
        }

        aliveBosses.clear();

        for (DungeonMob dungeonMob : bosses) {
            for (Location location : dungeonMob.getSpawnLocations()) {
                try {
                    Entity entity = mythicmobs.spawnMythicMob(dungeonMob.getName(), location);
                    aliveBosses.add(entity.getUniqueId());
                } catch (InvalidMobTypeException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void saveToDisk() {
        if (configFile == null) {
            configFile = new ConfigFile(new File(Dungeons.inst().getDataFolder() + "/dungeons", name + ".yml"));
        }
        configFile.set("Dungeon", this);
        configFile.save();
    }

    public Collection<Player> getPlayers() {
        Collection<Player> availablePlayers = new ArrayList<>();
        for (UUID uuid : dungeonPlayers) {
            if (Bukkit.getPlayer(uuid) != null) {
                availablePlayers.add(Bukkit.getPlayer(uuid));
            }
        }

        return availablePlayers;
    }

    public DungeonMob getMinion(String name) {
        for (DungeonMob dungeonMob : minions) {
            if (dungeonMob.getName().equals(name)) return dungeonMob;
        }

        return null;
    }

    public DungeonMob getBoss(String name) {
        for (DungeonMob dungeonMob : bosses) {
            if (dungeonMob.getName().equals(name)) return dungeonMob;
        }

        return null;
    }

    public boolean containsMinion(String name) {
        return minions.stream().anyMatch(dungeonMob -> dungeonMob.getName().equals(name));
    }

    public boolean containsBoss(String name) {
        return bosses.stream().anyMatch(dungeonMob -> dungeonMob.getName().equals(name));
    }

    public DungeonVillager getDungeonVillager() {
        return dungeonVillager;
    }

    public void setDungeonVillager(DungeonVillager dungeonVillager) {
        this.dungeonVillager = dungeonVillager;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getDungeonSpawn() {
        return dungeonSpawn;
    }

    public void setDungeonSpawn(Location dungeonSpawn) {
        this.dungeonSpawn = dungeonSpawn;
    }

    public DungeonState getState() {
        return state;
    }

    public void setState(DungeonState state) {
        this.state = state;
    }

    public int getPartySize() {
        return partySize;
    }

    public void setPartySize(int partySize) {
        this.partySize = partySize;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public List<DungeonMob> getMinions() {
        return minions;
    }

    public List<DungeonMob> getBosses() {
        return bosses;
    }

    public List<UUID> getDungeonPlayers() {
        return dungeonPlayers;
    }

    public List<UUID> getAliveMinions() {
        return aliveMobs;
    }

    public List<UUID> getAliveBosses() {
        return aliveBosses;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", name);
        map.put("villager", dungeonVillager.serialize());
        map.put("dungeonspawn", dungeonSpawn != null ? dungeonSpawn.serialize() : null);
        map.put("partysize", partySize);

        ArrayList<Map<String, Object>> minionsMap = new ArrayList<>();
        for (DungeonMob mob : minions) {
            minionsMap.add(mob.serialize());
        }
        map.put("minions", minionsMap);

        ArrayList<Map<String, Object>> bossesMap = new ArrayList<>();
        for (DungeonMob mob : bosses) {
            bossesMap.add(mob.serialize());
        }
        map.put("bosses", bossesMap);

        return map;
    }

    public static Dungeon deserialize(Map<String, Object> serializedDungeon) {

        String name = (String) serializedDungeon.get("name");
        Dungeon dungeon = new Dungeon(name);

        DungeonVillager villager = DungeonVillager.deserialize(dungeon, (Map<String, Object>) serializedDungeon.get("villager"));
        dungeon.setDungeonVillager(villager);

        dungeon.setDungeonSpawn(Location.deserialize((Map<String, Object>) serializedDungeon.get("dungeonspawn")));
        dungeon.setPartySize((Integer) serializedDungeon.get("partysize"));

        for (Map<String, Object> serializedMinion : (ArrayList<Map<String, Object>>) serializedDungeon.get("minions")) {
            dungeon.getMinions().add(DungeonMob.deserialize(serializedMinion));
        }

        for (Map<String, Object> serializedBoss : (ArrayList<Map<String, Object>>) serializedDungeon.get("bosses")) {
            dungeon.getBosses().add(DungeonMob.deserialize(serializedBoss));
        }

        return dungeon;
    }
}
