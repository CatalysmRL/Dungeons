package de.skyamogus.dungeons.dungeon.components;

import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.PartiesAPI;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import de.skyamogus.dungeons.dungeon.components.dungeoncomp.DungeonState;
import de.skyamogus.dungeons.util.ColorUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class DungeonVillager {

    private final Dungeon dungeon;
    private UUID uuid;
    private Villager villager;
    private String customName;
    private Location villagerLocation;
    private ItemStack villagerKey;

    public DungeonVillager(Dungeon dungeon) {
        this.dungeon = dungeon;
        customName = dungeon.getName();
    }

    public void handleInteract(PlayerInteractAtEntityEvent event) {
        event.setCancelled(true);

        Player player = event.getPlayer();

        if (dungeon.getState() != DungeonState.OPEN) {
            player.sendMessage(ColorUtils.color(String.format("&6[%s&6]&f: &7Another team is currently attempting to clear the dungeon", villager.getCustomName())));
            return;
        }

        if (villagerKey == null) {
            ColorUtils.sendMessage(player, String.format("&6[%s&6]&f: I do not offer a dungeon raid yet", villager.getCustomName()));
            return;
        }


        ItemStack item = player.getInventory().getItemInMainHand();

        if (!item.isSimilar(villagerKey)) {
            player.sendMessage(Component.text(ColorUtils.color("&6[" + customName + "&6]&f: &7To enter this dungeon u need a key: ")).append(villagerKey.displayName().hoverEvent(villagerKey.asHoverEvent())));
            return;
        }

        PartiesAPI partiesAPI = Parties.getApi();
        PartyPlayer partyPlayer = partiesAPI.getPartyPlayer(player.getUniqueId());

        if (!partyPlayer.isInParty()) {
            ColorUtils.sendMessage(player, String.format("&6[%s&6]&f: &7You need a party of &b" + dungeon.getPartySize() + " &7to enter this dungeon", villager.getCustomName()));
            return;
        }

        Party party = partiesAPI.getParty(partyPlayer.getPartyId());

        if (party.getOnlineMembers().size() != dungeon.getPartySize()) {
            for (PartyPlayer p : party.getOnlineMembers()) {
                ColorUtils.sendMessage(Bukkit.getPlayer(p.getPlayerUUID()), String.format("&6[%s&6]&f: &7You need a party of &b" + dungeon.getPartySize() + " &7to enter this dungeon", villager.getCustomName()));
            }
            return;
        }

        item.setAmount(item.getAmount() - 1);

        for (PartyPlayer p : party.getOnlineMembers()) {
            Bukkit.getPlayer(p.getPlayerUUID()).sendMessage(
                    Component.text(ColorUtils.color("&6[" + customName + "&6]&f: &8Entering the dungeon...")));
        }

        dungeon.setParty(party);
        dungeon.start();
    }

    public void spawnVillager(Location location) {
        removeVillager();

        villagerLocation = location;
        villager = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
        uuid = villager.getUniqueId();
        villager.setAI(false);
        villager.setCustomName(customName);
        villager.setCustomNameVisible(true);
        villager.setInvulnerable(true);
        villager.setSilent(true);
    }

    public void removeVillager() {
        if (villager == null) {
            if (uuid == null) return;
            if (Bukkit.getEntity(uuid) == null) return;
            Bukkit.getEntity(uuid).remove();
        } else {
            villager.remove();
            villager = null;
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Villager getVillager() {
        return villager;
    }

    public void setVillager(Villager villager) {
        this.villager = villager;
    }

    public Location getVillagerLocation() {
        return villagerLocation;
    }

    public void setVillagerLocation(Location villagerLocation) {
        this.villagerLocation = villagerLocation;
    }

    public ItemStack getVillagerKey() {
        return villagerKey;
    }

    public void setVillagerKey(ItemStack villagerKey) {
        this.villagerKey = villagerKey;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
        getVillager().setCustomName(ColorUtils.color(customName));
    }

    public Map<String, Object> serialize() {

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("uuid", villager.getUniqueId().toString());
        map.put("customname", customName);
        map.put("location", villagerLocation != null ? villagerLocation : null);
        map.put("key", villagerKey != null ? villagerKey : null);

        return map;
    }

    public static DungeonVillager deserialize(Dungeon dungeon, Map<String, Object> serializedVillager) {

        UUID uuid = UUID.fromString((String) serializedVillager.get("uuid"));
        String customName = (String) serializedVillager.get("customname");
        Location villagerLocation = (Location) serializedVillager.get("location");
        ItemStack villagerKey = (ItemStack) serializedVillager.get("key");

        DungeonVillager dungeonVillager = new DungeonVillager(dungeon);
        dungeonVillager.setUuid(uuid);
        dungeonVillager.setVillagerKey(villagerKey);
        dungeonVillager.spawnVillager(villagerLocation);
        dungeonVillager.setCustomName(customName);

        return dungeonVillager;
    }
}
