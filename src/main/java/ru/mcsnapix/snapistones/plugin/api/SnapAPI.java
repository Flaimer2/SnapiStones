package ru.mcsnapix.snapistones.plugin.api;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.World;
import ru.mcsnapix.snapistones.plugin.api.region.Region;
import ru.mcsnapix.snapistones.plugin.api.region.RegionRegistry;
import ru.mcsnapix.snapistones.plugin.util.WGRegionUtil;

@UtilityClass
public class SnapApi {
    public Region getRegion(World world, String id) {
        return RegionRegistry.get().getRegion(world, id);
    }

    public Region getRegion(Location location) {
        World world = location.getWorld();
        ProtectedRegion protectedRegion = WGRegionUtil.getRegion(location);
        if (protectedRegion == null) return null;

        return RegionRegistry.get().getRegion(world, protectedRegion.getId());
    }
}
