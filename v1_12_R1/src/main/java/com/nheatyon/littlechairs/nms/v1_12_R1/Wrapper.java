package com.nheatyon.littlechairs.nms.v1_12_R1;

import com.nheatyon.littlechairs.nms.VersionWrapper;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntity.PacketPlayOutEntityLook;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

public final class Wrapper implements VersionWrapper {

    @Override
    public ArmorStand spawnStand(final Player p, final Location location) {
        return p.getWorld().spawn(location, ArmorStand.class, (stand) -> {
            stand.setVisible(false);
            stand.setSmall(true);
            stand.setGravity(false);
            stand.setCanPickupItems(false);
            stand.setMarker(true);
            stand.setBasePlate(false);
        });
    }

    @Override
    public void setYaw(final Player p, final ArmorStand stand) {
        byte yaw = (byte) p.getLocation().getYaw();
        byte pitch = (byte) stand.getLocation().getPitch();
        PacketPlayOutEntityLook packet = new PacketPlayOutEntityLook(stand.getEntityId(), yaw, pitch, true);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }

}
