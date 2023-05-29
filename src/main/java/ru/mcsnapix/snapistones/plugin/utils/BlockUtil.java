package ru.mcsnapix.snapistones.plugin.utils;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.api.ProtectedBlock;
import ru.mcsnapix.snapistones.plugin.settings.config.blocks.BlockConfig;
import ru.mcsnapix.snapistones.plugin.settings.config.blocks.BlockOptions;
import ru.mcsnapix.snapistones.plugin.xseries.XMaterial;

import java.util.Map;

@UtilityClass
public class BlockUtil {
    private final SnapiStones plugin = SnapiStones.get();
    private final BlockConfig blockConfig = plugin.blockConfig().data();
    private final Map<XMaterial, ProtectedBlock> protectedBlockMap = plugin.protectedBlockMap();

    public boolean isProtectedBlock(XMaterial xMaterial) {
        return protectedBlockMap.containsKey(xMaterial);
    }

    public ProtectedBlock protectedBlock(XMaterial xMaterial) {
        return protectedBlockMap.get(xMaterial);
    }

    public ProtectedBlock protectedBlock(Location location) {
        Block block = location.getWorld().getBlockAt(location);
        XMaterial item = XMaterial.matchXMaterial(block.getType());
        return protectedBlock(item);
    }

    public BlockOptions blockOptions(XMaterial xMaterial) {
        return blockConfig.blocks().get(xMaterial.name());
    }

    public boolean isRegionProtectedBlock(Location location) {
        World world = location.getWorld();
        RegionUtil regionUtil = new RegionUtil(world);
        ProtectedRegion region = regionUtil.getRegion(location);

        if (region == null) {
            return false;
        }

        Location center = regionUtil.getCenter(region);

        return center.toString().equals(location.toString());
    }
}