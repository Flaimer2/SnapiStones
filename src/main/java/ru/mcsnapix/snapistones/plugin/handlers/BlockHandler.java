package ru.mcsnapix.snapistones.plugin.handlers;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.tr7zw.changeme.nbtapi.NBT;
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
import ru.mcsnapix.snapistones.plugin.Placeholders;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.api.SnapApi;
import ru.mcsnapix.snapistones.plugin.api.events.region.RegionCreateEvent;
import ru.mcsnapix.snapistones.plugin.api.region.Region;
import ru.mcsnapix.snapistones.plugin.api.region.RegionRegistry;
import ru.mcsnapix.snapistones.plugin.database.Database;
import ru.mcsnapix.snapistones.plugin.settings.config.MainConfig;
import ru.mcsnapix.snapistones.plugin.settings.config.block.BlockOption;
import ru.mcsnapix.snapistones.plugin.settings.message.Message;
import ru.mcsnapix.snapistones.plugin.util.BlockUtil;
import ru.mcsnapix.snapistones.plugin.util.WGRegionUtil;
import ru.mcsnapix.snapistones.plugin.xseries.XMaterial;

@RequiredArgsConstructor
public class BlockHandler implements Listener {
    @NonNull
    private final SnapiStones plugin;

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        MainConfig config = plugin.getMainConfig().data();
        Message message = plugin.getMessage().data();
        WorldGuardPlugin worldGuard = plugin.getWorldGuard();

        Player player = event.getPlayer();
        Placeholders placeholders = new Placeholders(player);
        LocalPlayer localPlayer = WGRegionUtil.getLocalPlayer(player);
        Block block = event.getBlockPlaced();
        ItemStack itemInHand = event.getItemInHand();

        XMaterial xMaterial = XMaterial.matchXMaterial(block.getType());

        Location location = block.getLocation();
        World world = location.getWorld();
        double blockX = location.getX();
        double blockY = location.getY();
        double blockZ = location.getZ();
        BlockOption blockOption = BlockUtil.getBlockOption(xMaterial);

        if (blockOption == null) return;
        if (!config.enableWorld().contains(world.getName())) return;
        if (!blockOption.alwaysCreateRegion() && player.isSneaking()) return;
        if (!worldGuard.createProtectionQuery().testBlockPlace(player, block.getLocation(), block.getType())) return;

        if (SnapApi.getMaxRegionCount(player) < SnapApi.getRegionsByOwner(player.getName()).size()) {
            placeholders.sendMessage(message.maxRegionCount());
            return;
        }

        RegionManager regionManager = worldGuard.getRegionManager(world);

        String id = WGRegionUtil.createRegionID(regionManager, player, blockOption.symbol());
        BlockVector min = WGRegionUtil.getMinVector(blockX, blockY, blockZ, blockOption.radius());
        BlockVector max = WGRegionUtil.getMaxVector(blockX, blockY, blockZ, blockOption.radius());

        ProtectedRegion protectedRegion = new ProtectedCuboidRegion(id, min, max);
        ApplicableRegionSet regions = regionManager.getApplicableRegions(protectedRegion);

        if (regions.size() > 0) {
            event.setCancelled(true);
            placeholders.sendMessage(message.cannotPlaceProtectedBlock());
            return;
        }

        Database database = new Database(id);

        protectedRegion.getOwners().addPlayer(localPlayer);
        regionManager.addRegion(protectedRegion);
        database.createRegion(player.getName(), location, xMaterial.name());
        RegionRegistry.get().addRegion(id, protectedRegion);
        Region region = SnapApi.getRegion(id);

        // ! Module Upgrade
        if (itemInHand != null) {
            String effect = NBT.get(itemInHand, nbt -> nbt.getString("effect"));
            if (!effect.isEmpty()) {
                region.setEffects(effect);
            }

            String maxOwner = NBT.get(itemInHand, nbt -> nbt.getString("maxOwner"));
            if (!maxOwner.isEmpty()) {
                region.maxOwners(Integer.parseInt(maxOwner));
            }
            String maxMember = NBT.get(itemInHand, nbt -> nbt.getString("maxMember"));
            if (!maxMember.isEmpty()) {
                region.maxMembers(Integer.parseInt(maxMember));
            }
        }

        plugin.callEvent(new RegionCreateEvent(player, region));
    }
}
