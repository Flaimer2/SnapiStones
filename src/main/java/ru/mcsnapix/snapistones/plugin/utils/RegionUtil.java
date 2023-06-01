package ru.mcsnapix.snapistones.plugin.utils;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.GlobalProtectedRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.settings.config.MainConfig;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Accessors(fluent = true)
@Getter
public class RegionUtil {
    private final SnapiStones plugin = SnapiStones.get();
    private final WorldGuardPlugin worldGuard = plugin.worldGuard();
    private final MainConfig config = plugin.mainConfig().data();
    private final World world;
    private final RegionManager regionManager;

    public RegionUtil(World world) {
        this.world = world;
        regionManager = worldGuard.getRegionManager(world);
    }

    public RegionUtil(Player player) {
        this(player.getWorld());
    }

    public BlockVector getMinVector(double bx, double by, double bz, long radius) {
        return new BlockVector(bx - radius, by - radius, bz - radius);
    }

    public BlockVector getMaxVector(double bx, double by, double bz, long radius) {
        return new BlockVector(bx + radius, by + radius, bz + radius);
    }

    public Location getCenter(ProtectedRegion region) {
        if (region instanceof GlobalProtectedRegion) {
            return null;
        }
        return getCenter(region.getMinimumPoint(), region.getMaximumPoint());
    }

    public Location getCenter(BlockVector vMin, BlockVector vMax) {
        int cenX = (int) (vMin.getX() + vMax.getX()) / 2;
        int cenY = (int) (vMin.getY() + vMax.getY()) / 2;
        int cenZ = (int) (vMin.getZ() + vMax.getZ()) / 2;

        return new Location(world, cenX, cenY, cenZ);
    }

    public ProtectedRegion getRegion(String id) {
        return regionManager.getRegion(id);
    }

    public ProtectedRegion getRegion(Location location) {
        ApplicableRegionSet set = worldGuard.getRegionContainer().createQuery()
                .getApplicableRegions(location);
        ProtectedRegion rg = null;

        for (ProtectedRegion region : set) {
            rg = region;
        }

        return rg;
    }

    public boolean hasRegion(String id) {
        return regionManager.hasRegion(id);
    }

    public List<ProtectedRegion> getRegions() {
        List<ProtectedRegion> regions = new ArrayList<>();
        Set<String> disabledRegions = new HashSet<>(config.disableRegion());

        for (ProtectedRegion pr : regionManager.getRegions().values()) {
            if (pr.getId().equalsIgnoreCase(ProtectedRegion.GLOBAL_REGION) || disabledRegions.contains(pr.getId())) {
                continue;
            }

            regions.add(pr);
        }
        return regions;
    }

    public ProtectedRegion getGlobalRegion() {
        return getRegion(ProtectedRegion.GLOBAL_REGION);
    }

    public RegionManager regionManager() {
        return regionManager;
    }
}