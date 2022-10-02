package com.nheatyon.littlechairs.nms;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

public interface VersionWrapper {

    ArmorStand spawnStand(Player p, Location location);
    void setYaw(Player p, ArmorStand stand);
}
