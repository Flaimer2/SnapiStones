package ru.mcsnapix.snapistones.plugin.api.region;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.Getter;
import lombok.NonNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RegionRegistry {
    private static RegionRegistry INSTANCE;
    @Getter
    private final Map<String, Region> regionMap = new ConcurrentHashMap<>();

    private RegionRegistry() {
    }

    public static synchronized RegionRegistry get() {
        if (INSTANCE == null) {
            INSTANCE = new RegionRegistry();
        }
        return INSTANCE;
    }

    public Region getRegion(@NonNull String id) {
        return regionMap.get(id);
    }

    public void addRegion(@NonNull String id, @NonNull ProtectedRegion region) {
        regionMap.putIfAbsent(id, new Region(id, region));
    }

    public void removeRegion(@NonNull String id) {
        regionMap.remove(id);
    }
}
