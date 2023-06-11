package ru.mcsnapix.snapistones.plugin.handlers;

import com.sk89q.worldguard.protection.managers.RegionManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import ru.mcsnapix.snapistones.plugin.ClickAction;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.api.ProtectedBlock;
import ru.mcsnapix.snapistones.plugin.api.SnapApi;
import ru.mcsnapix.snapistones.plugin.api.events.block.BlockInteractEvent;
import ru.mcsnapix.snapistones.plugin.api.events.region.RegionRemoveEvent;
import ru.mcsnapix.snapistones.plugin.api.region.Region;
import ru.mcsnapix.snapistones.plugin.database.Database;
import ru.mcsnapix.snapistones.plugin.util.WGRegionUtil;

import java.util.List;

@RequiredArgsConstructor
public class ProtectedBlockHandler implements Listener {
    @NonNull
    private final SnapiStones plugin;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();

        if (!(action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK)) return;

        Block block = event.getClickedBlock();
        if (block == null) return;

        Location location = block.getLocation();
        Region region = SnapApi.getRegion(location);
        if (region == null) return;
        ProtectedBlock protectedBlock = region.protectedBlock();
        if (protectedBlock.center() != location) return;

        Player player = event.getPlayer();
        if (player == null) return;
        if (!region.hasPlayerInRegion(player.getName())) return;

        boolean owner;

        if (region.hasOwnerInRegion(player.getName())) owner = true;

        ClickAction clickAction = ClickAction.getClickAction(player, action);
        plugin.callEvent(new BlockInteractEvent(player, clickAction, region, owner));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block == null) return;

        Location location = block.getLocation();
        Region region = SnapApi.getRegion(location);
        if (region == null) return;
        ProtectedBlock protectedBlock = region.protectedBlock();
        if (protectedBlock.center() != location) return;

        RegionManager regionManager = WGRegionUtil.getRegionManager(location.getWorld());
        event.setCancelled(true);

        Player player = event.getPlayer();
        if (player == null) return;
        if (!region.hasOwnerInRegion(player.getName())) {
            return;
        }

        block.setType(Material.AIR);
        // ! Module Upgrade
        List<String> effects = region.effects();
        int maxOwners = region.maxOwners();
        int maxMembers = region.maxMembers();

        UpgradeModule upgradeModule = plugin.module().upgrade();
        UpgradeConfig upgradeConfig = upgradeModule.upgradeConfig().data();
        var itemConfig = upgradeConfig.item();

        ItemStack itemDrop = item.parseItem();
        ItemMeta itemMeta = itemDrop.getItemMeta();
        itemMeta.setDisplayName(itemConfig.name());

        ReplacedList lore = new ReplacedList(itemConfig.lore());

        if (effects != null) {
            ReplacedList effectBought = new ReplacedList(itemConfig.effectBought());
            effectBought = effectBought.replace("%effects%", FormatterUtil.formatList(effects, itemConfig.formatListEffect()));
            lore = lore.replace("%effectBought%", effectBought);
        } else {
            lore = lore.replace("%effectBought%", (List<String>) null);
        }

        lore = lore.replace("%maxOwners%", itemConfig.maxOwners().replace("%amount%", Integer.toString(maxOwners)));
        lore = lore.replace("%maxMembers%", itemConfig.maxMembers().replace("%amount%", Integer.toString(maxMembers)));

        itemMeta.setLore(lore);
        itemDrop.setItemMeta(itemMeta);

        NBTItem nbti = new NBTItem(itemDrop);

        if (effects != null) {
            nbti.setString("effect", ListSerializer.serialize(effects));
        }
        nbti.setString("maxOwner", Integer.toString(maxOwners));
        nbti.setString("maxMember", Integer.toString(maxMembers));

        block.getWorld().dropItem(block.getLocation(), nbti.getItem());

        plugin.callEvent(new RegionRemoveEvent(player, region));
        regionManager.removeRegion(region.name());
        Database database = new Database(region.name());
        database.removeRegion();
    }

    @EventHandler
    public void onBlockExplode(EntityExplodeEvent event) {
        event.blockList().removeIf(
                block ->
                        BlockUtil.isRegionProtectedBlock(block.getLocation())
        );
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent e) {
        cancelEventIfProtectedBlocksPresent(e.getBlocks(), e);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPistonRetract(BlockPistonRetractEvent e) {
        cancelEventIfProtectedBlocksPresent(e.getBlocks(), e);
    }

    private void cancelEventIfProtectedBlocksPresent(List<Block> pushedBlocks, BlockPistonEvent event) {
        for (Block block : pushedBlocks) {
            if (BlockUtil.isRegionProtectedBlock(block.getLocation())) {
                event.setCancelled(true);
            }
        }
    }
}
