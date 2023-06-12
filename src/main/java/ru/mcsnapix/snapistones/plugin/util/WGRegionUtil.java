package ru.mcsnapix.snapistones.plugin.util;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.settings.config.MainConfig;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@UtilityClass
public class WGRegionUtil {
    private final SnapiStones plugin = SnapiStones.get();
    private final WorldGuardPlugin worldGuard = plugin.getWorldGuard();
    private final MainConfig config = plugin.getMainConfig().data();

    public @Nullable ProtectedRegion getRegion(World world, String id) {
        RegionManager regionManager = getRegionManager(world);
        return regionManager.getRegion(id);
    }

    public @Nullable ProtectedRegion getRegion(Location location) {
        if (!config.enableWorld().contains(location.getWorld().getName())) return null;
        ApplicableRegionSet set = worldGuard.getRegionContainer().createQuery()
                .getApplicableRegions(location);
        ProtectedRegion rg = null;

        for (ProtectedRegion region : set) {
            rg = region;
        }

        if (rg == null) return null;
        if (config.disableRegion().contains(rg.getId())) return null;

        return rg;
    }

    public boolean hasRegion(World world, String id) {
        if (!config.enableWorld().contains(world.getName())) return false;
        if (!config.disableRegion().contains(id)) return false;

        RegionManager regionManager = getRegionManager(world);
        return regionManager.hasRegion(id);
    }

    public Location getCenter(World world, BlockVector vMin, BlockVector vMax) {
        int cenX = (int) (vMin.getX() + vMax.getX()) / 2;
        int cenY = (int) (vMin.getY() + vMax.getY()) / 2;
        int cenZ = (int) (vMin.getZ() + vMax.getZ()) / 2;

        return new Location(world, cenX, cenY, cenZ);
    }

    public BlockVector getMinVector(double bx, double by, double bz, long radius) {
        return new BlockVector(bx - radius, by - radius, bz - radius);
    }

    public BlockVector getMaxVector(double bx, double by, double bz, long radius) {
        return new BlockVector(bx + radius, by + radius, bz + radius);
    }

    public Location getCenter(World world, ProtectedRegion region) {
        BlockVector vMin = region.getMinimumPoint();
        BlockVector vMax = region.getMaximumPoint();

        int cenX = (int) (vMin.getX() + vMax.getX()) / 2;
        int cenY = (int) (vMin.getY() + vMax.getY()) / 2;
        int cenZ = (int) (vMin.getZ() + vMax.getZ()) / 2;

        return new Location(world, cenX, cenY, cenZ);
    }

    public String createRegionID(RegionManager regionManager, Player player, String symbol) {
        LocalPlayer localPlayer = getLocalPlayer(player);
        int regionCount = regionManager.getRegionCountOfPlayer(localPlayer) + 1;
        return (player.getName() + "_" + symbol + regionCount).toLowerCase();
    }

    public LocalPlayer getLocalPlayer(Player player) {
        return worldGuard.wrapPlayer(player);
    }

    public String createRegionID(World world, Player player, String symbol) {
        return createRegionID(getRegionManager(world), player, symbol);
    }

    public RegionManager getRegionManager(World world) {
        return worldGuard.getRegionManager(world);
    }

    public List<ProtectedRegion> getRegions(World world) {
        List<ProtectedRegion> regions = new ArrayList<>();
        Set<String> disabledRegions = new HashSet<>(config.disableRegion());

        for (ProtectedRegion pr : getRegionManager(world).getRegions().values()) {
            if (pr.getId().equalsIgnoreCase(ProtectedRegion.GLOBAL_REGION) || disabledRegions.contains(pr.getId())) {
                continue;
            }

            regions.add(pr);
        }
        return regions;
    }
}
