package ru.mcsnapix.snapistones.plugin.api;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.api.region.Region;
import ru.mcsnapix.snapistones.plugin.api.region.RegionRegistry;
import ru.mcsnapix.snapistones.plugin.settings.config.MainConfig;
import ru.mcsnapix.snapistones.plugin.util.WGRegionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class SnapApi {
    private final MainConfig config = SnapiStones.get().getMainConfig().data();
    private final RegionRegistry regionRegistry = RegionRegistry.get();

    public Region getRegion(String id) {
        return regionRegistry.getRegion(id.toLowerCase());
    }

    public Region getRegion(Location location) {
        ProtectedRegion protectedRegion = WGRegionUtil.getRegion(location);
        if (protectedRegion == null) return null;
        return getRegion(protectedRegion.getId());
    }

    public List<Region> getRegions() {
        return new ArrayList<>(regionRegistry.getRegionMap().values());
    }

    public List<Region> getRegionsByPlayer(String player) {
        return getRegions().stream().filter(region -> region.hasPlayerInRegion(player)).collect(Collectors.toList());
    }

    public List<Region> getRegionsByOwner(String owner) {
        return getRegions().stream().filter(region -> region.hasOwnerInRegion(owner)).collect(Collectors.toList());
    }

    public List<Region> getRegionsByMember(String member) {
        return getRegions().stream().filter(region -> region.hasMemberInRegion(member)).collect(Collectors.toList());
    }

    public boolean isProtectedBlock(Location location) {
        Region region = SnapApi.getRegion(location);
        if (region == null) return false;
        ProtectedBlock protectedBlock = region.protectedBlock();

        return protectedBlock.center().equals(location.getBlock().getLocation());
    }

    public int getMaxRegionCount(Player player) {
        int count = config.regionCount().get("default");

        for (var entry : config.regionCount().entrySet()) {
            String group = entry.getKey();
            int fCount = entry.getValue();

            if (player.hasPermission("snapistones.region.count." + group)) {
                if (count < fCount) count = fCount;
            }
        }

        return count;
    }
}
