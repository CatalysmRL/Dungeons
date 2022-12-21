package de.skyamogus.dungeons.listeners;

import de.skyamogus.dungeons.Dungeons;
import de.skyamogus.dungeons.dungeon.components.Dungeon;
import de.skyamogus.dungeons.dungeon.components.dungeoncomp.DungeonState;
import de.skyamogus.dungeons.util.ColorUtils;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

public class DungeonListener implements Listener {

    private final Dungeons plugin;

    public DungeonListener(Dungeons plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMythicDeath(MythicMobDeathEvent event) {
        for (Dungeon dungeon : plugin.getDungeonManager().dungeons) {
            DungeonState state = dungeon.getState();
            if (state == DungeonState.OPEN) {
            } else if (state == DungeonState.MINION) {
                List<UUID> aliveMinions = dungeon.getAliveMinions();
                aliveMinions.remove(event.getEntity().getUniqueId());
                if (aliveMinions.size() == 0) {
                    dungeon.getPlayers().forEach(player -> ColorUtils.sendMessage(player, String.format("&6[%s&6]&f: &7Minion stage cleared!", dungeon.getDungeonVillager().getCustomName())));
                    dungeon.setState(DungeonState.BOSS);
                    dungeon.spawnBosses();
                }
            } else if (state == DungeonState.BOSS) {
                List<UUID> aliveBosses = dungeon.getAliveBosses();
                aliveBosses.remove(event.getEntity().getUniqueId());
                if (aliveBosses.size() == 0) {
                    dungeon.getPlayers().forEach(player -> ColorUtils.sendMessage(player, String.format("&6[%s&6]&f: &7Congratulations, you cleared the dungeon!", dungeon.getDungeonVillager().getCustomName())));
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        for (UUID uuid : dungeon.getDungeonPlayers()) {
                            if (Bukkit.getPlayer(uuid) != null) {
                                Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "spawn " + Bukkit.getPlayer(uuid).getName());
                            }
                        }

                        dungeon.setState(DungeonState.OPEN);
                    }, 100);
                    new BukkitRunnable() {

                        int countdown = 5;

                        @Override
                        public void run() {
                            Component mainTitle = Component.text(countdown).color(TextColor.color(0, 255, 0));
                            Component subTitle = Component.text("Sending you back to spawn...").color(TextColor.color(255, 255, 0));

                            Title title = Title.title(mainTitle, subTitle);

                            for (Player player : dungeon.getPlayers()) {
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
                                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                    dungeon.getPlayers().forEach(player -> player.performCommand("spawn"));
                                }, 2L);
                                cancel();
                            }

                            countdown--;
                        }
                    }.runTaskTimer(Dungeons.inst(), 0L, 20L);
                }
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for (Dungeon dungeon : plugin.getDungeonManager().dungeons) {
            if (dungeon.getState() == DungeonState.OPEN) continue;
            if (dungeon.getDungeonPlayers().contains(event.getPlayer().getUniqueId())) {
                ColorUtils.sendMessage(player, plugin.PREFIX + "Sending you back to the dungeon...");
                Bukkit.getScheduler().runTaskLater(plugin, () -> player.teleportAsync(dungeon.getDungeonSpawn()), 60L);
                return;
            }
        }
    }


}
