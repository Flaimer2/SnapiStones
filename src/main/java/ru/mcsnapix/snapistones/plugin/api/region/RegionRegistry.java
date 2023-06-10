package ru.mcsnapix.snapistones.plugin.api.region;

import org.bukkit.World;
import ru.mcsnapix.snapistones.plugin.util.WGRegionUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RegionRegistry {
    private static RegionRegistry INSTANCE;
    private final Map<String, Region> regionMap = new ConcurrentHashMap<>();

    private RegionRegistry() {
    }

    public static synchronized RegionRegistry get() {
        if (INSTANCE == null) {
            INSTANCE = new RegionRegistry();
        }
        return INSTANCE;
    }

    public Region getRegion(World world, String id) {
        if (id == null) return null;
        if (!WGRegionUtil.hasRegion(world, id)) return null;

        return regionMap.putIfAbsent(id, new Region(id, WGRegionUtil.getRegion(world, id)));
    }
}
