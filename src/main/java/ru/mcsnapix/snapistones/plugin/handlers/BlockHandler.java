package ru.mcsnapix.snapistones.plugin.handlers;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.api.ProtectedBlock;
import ru.mcsnapix.snapistones.plugin.api.SnapAPI;
import ru.mcsnapix.snapistones.plugin.api.SnapPlayer;
import ru.mcsnapix.snapistones.plugin.api.events.region.RegionCreateEvent;
import ru.mcsnapix.snapistones.plugin.database.Database;
import ru.mcsnapix.snapistones.plugin.settings.config.MainConfig;
import ru.mcsnapix.snapistones.plugin.settings.message.Message;
import ru.mcsnapix.snapistones.plugin.utils.BlockUtil;
import ru.mcsnapix.snapistones.plugin.utils.ItemUtil;
import ru.mcsnapix.snapistones.plugin.utils.RegionUtil;
import ru.mcsnapix.snapistones.plugin.xseries.XMaterial;

@RequiredArgsConstructor
public class BlockHandler implements Listener {
    @NonNull
    private final SnapiStones plugin;
    private final MainConfig config = plugin.mainConfig().data();
    private final Message message = plugin.message().data();

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();
        XMaterial xMaterial = XMaterial.matchXMaterial(block.getType());

        if (!BlockUtil.isProtectedBlock(xMaterial)) {
            return;
        }

        if (!config.enableWorld().contains(block.getWorld().getName())) {
            return;
        }

        if (player.isSneaking()) {
            return;
        }

        event.setCancelled(true);

        if (!plugin.worldGuard().createProtectionQuery().testBlockPlace(player, block.getLocation(), block.getType())) {
            return;
        }

        ProtectedBlock protectedBlock = BlockUtil.protectedBlock(xMaterial);
        Location location = block.getLocation();

        World world = location.getWorld();
        double blockX = location.getX();
        double blockY = location.getY();
        double blockZ = location.getZ();

        RegionUtil regionUtil = new RegionUtil(world);
        RegionManager rm = regionUtil.regionManager();
        ItemStack itemInHand = event.getItemInHand();
        SnapPlayer snapPlayer = SnapAPI.player(player);

        String id = snapPlayer.createRegionID(protectedBlock.symbol());

        BlockVector min = regionUtil.getMinVector(blockX, blockY, blockZ, protectedBlock.radius());
        BlockVector max = regionUtil.getMaxVector(blockX, blockY, blockZ, protectedBlock.radius());

        ProtectedRegion region = new ProtectedCuboidRegion(id, min, max);
        snapPlayer = SnapAPI.player(player, region, protectedBlock);

        ApplicableRegionSet regions = rm.getApplicableRegions(region);
        if (regions.size() > 0) {
            snapPlayer.sendMessage(message.cannotPlaceProtectedBlock());
            event.setCancelled(true);
        }

        region.getOwners().addPlayer(snapPlayer.localPlayer());
        rm.addRegion(region);

        // ! Module Upgrade
        Database database = new Database(region);

        if (itemInHand != null) {
            if (ItemUtil.hasTag(itemInHand, "effect")) {
                String effects = ItemUtil.nbtTag(itemInHand, "effect");
                database.effects(effects);
            }

            if (ItemUtil.hasTag(itemInHand, "maxOwner")) {
                String maxOwner = ItemUtil.nbtTag(itemInHand, "maxOwner");
                database.maxOwners(maxOwner);
            }

            if (ItemUtil.hasTag(itemInHand, "maxMember")) {
                String maxMember = ItemUtil.nbtTag(itemInHand, "maxMember");
                database.maxMembers(maxMember);
            }

            ItemStack item = itemInHand.clone();
            item.setAmount(1);
            player.getInventory().removeItem(item);
            block.setType(xMaterial.parseMaterial());
        }

        plugin.callEvent(new RegionCreateEvent(snapPlayer, region, protectedBlock, location));
    }
}