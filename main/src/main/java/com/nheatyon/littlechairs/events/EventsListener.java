package com.nheatyon.littlechairs.events;

import com.nheatyon.littlechairs.InitializerClass;
import com.nheatyon.littlechairs.nms.VersionWrapper;
import com.nheatyon.littlechairs.utils.ChairsData;
import com.nheatyon.littlechairs.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class EventsListener implements Listener {

    private final InitializerClass main;
    private final VersionWrapper wrapper = InitializerClass.getWrapper();
    private final ConfigManager config = InitializerClass.getConfigManager();

    private final ChairsData chairs = new ChairsData();
    private final List<Player> inWaiting = new ArrayList<>();
    private int standTask;

    public EventsListener(final InitializerClass main) {
        this.main = main;
    }

    @EventHandler
    public void onInteract(final PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        Block clickedBlock = e.getClickedBlock();

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && clickedBlock.toString().contains("STAIRS")) {
            boolean isInverted = clickedBlock.getState().getData().toString().contains("inverted");
            if (!isInverted) {
                if (!chairs.getOccupied().containsValue(clickedBlock)) {
                    Location spawnLocation = clickedBlock.getLocation().add(0.5, 0.3, 0.5);
                    ArmorStand stand = wrapper.spawnStand(p, spawnLocation);
                    stand.setPassenger(p);
                    standTask = Bukkit.getScheduler().runTaskTimerAsynchronously(main, () -> wrapper.setYaw(p, stand), 4, 4).getTaskId();
                    chairs.getSitting().put(uuid, stand);
                    chairs.getOccupied().put(uuid, clickedBlock);
                } else {
                    if (chairs.getOccupied().get(uuid) == null) {
                        chairs.sendChairOccupied(inWaiting, config, p);
                    } else {
                        if (!p.isInsideVehicle() && chairs.getOccupied().containsValue(clickedBlock)) {
                            chairs.getSitting().remove(uuid);
                            chairs.getOccupied().remove(uuid);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDismount(final EntityDismountEvent e) {
        chairs.removeSit(e.getEntity(), e, standTask);
    }

    @EventHandler
    public void onDeath(final PlayerDeathEvent e) {
        chairs.removeSit(e.getEntity(), e, standTask);
    }

    @EventHandler
    public void onBreak(final BlockBreakEvent e) {
        for (Block occupiedBlock : chairs.getOccupied().values()) {
            if (chairs.getOccupied().containsValue(e.getBlock()) && e.getBlock().equals(occupiedBlock)) {
                e.setCancelled(true);
                chairs.sendChairOccupied(inWaiting, config, e.getPlayer());
            }
        }
    }

    @EventHandler
    public void onTeleport(final PlayerTeleportEvent e) {
        chairs.removeSit(e.getPlayer(), e, standTask);
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        chairs.removeSit(e.getPlayer(), e, standTask);
    }
}
