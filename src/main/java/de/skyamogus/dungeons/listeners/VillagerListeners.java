package de.skyamogus.dungeons.listeners;

import de.skyamogus.dungeons.Dungeons;
import de.skyamogus.dungeons.dungeon.components.Dungeon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class VillagerListeners implements Listener {

    private final Dungeons plugin;

    public VillagerListeners(Dungeons plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (entity.getType() != EntityType.VILLAGER) return;

        for (Dungeon dungeon : plugin.getDungeonManager().dungeons) {
            if (dungeon.getDungeonVillager().getVillager().equals(entity)) {
                dungeon.getDungeonVillager().handleInteract(event);
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntityType() != EntityType.VILLAGER) return;
        for (Dungeon dungeon : plugin.getDungeonManager().dungeons) {
            if (dungeon.getDungeonVillager().getVillager().equals(event.getEntity())) {
                event.setCancelled(true);
                return;
            }
        }
    }

}
