package com.nheatyon.littlechairs.utils;

import com.nheatyon.littlechairs.InitializerClass;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class ChairsData {

    @Getter private final HashMap<UUID, ArmorStand> sitting = new HashMap<>();
    @Getter private final HashMap<UUID, Block> occupied = new HashMap<>();
    private final InitializerClass main = InitializerClass.getInstance();

    public void removeSit(final Entity entity, final Event event, final int standTask) {
        if (entity instanceof Player) {
            UUID uuid = entity.getUniqueId();
            if (sitting.containsKey(uuid)) {
                Bukkit.getScheduler().runTaskLater(main, () -> {
                    if (!(event.getEventName().equals("EntityDismountEvent"))) {
                        entity.leaveVehicle();
                    } else {
                        // Avoid "Moved wrongly" warning
                        Location loc = entity.getLocation();
                        loc.setY(loc.getY() + 0.05);
                        entity.teleport(loc);
                    }
                }, 1L);
                if (sitting.get(uuid) != null) sitting.get(uuid).remove();
                Bukkit.getScheduler().cancelTask(standTask);
                sitting.remove(uuid);
                occupied.remove(uuid);
            }
        }
    }

    public void sendChairOccupied(final List<Player> inWaiting, final ConfigManager config, final Player p) {
        if (config.getValue("enable_sounds")) {
            Sound sound;
            try {
                sound = Sound.valueOf(config.getValue("occupied_sound"));
                p.playSound(p.getLocation(), sound, 1, 1);
            } catch (IllegalArgumentException e) {
                Bukkit.getConsoleSender().sendMessage(config.getMessage("invalid_sound"));
            }
        }
        if (!inWaiting.contains(p)) {
            p.sendMessage(config.getMessage("occupied_msg"));
            inWaiting.add(p);
            // 20 ticks = 1 second
            Bukkit.getScheduler().runTaskLater(main, () -> inWaiting.remove(p), 60L);
        }
    }

}
