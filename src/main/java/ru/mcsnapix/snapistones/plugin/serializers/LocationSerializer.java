package ru.mcsnapix.snapistones.plugin.serializers;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

@UtilityClass
public class LocationSerializer {
    public String serialise(Location location) {
        return location.getX() + ";" + location.getY() + ";" + location.getZ() + ";" + location.getWorld().getName() + ";" + location.getYaw() + ";" + location.getPitch();
    }

    public Location deserialise(String s) {
        String[] parts = s.split(";");
        if (parts.length == 0) {
            return null;
        }
        double x = Double.parseDouble(parts[0]);
        double y = Double.parseDouble(parts[1]);
        double z = Double.parseDouble(parts[2]);
        World world = Bukkit.getServer().getWorld(parts[3]);
        float yaw = Float.parseFloat(parts[4]);
        float pitch = Float.parseFloat(parts[5]);

        return new Location(world, x, y, z, yaw, pitch);
    }
}
