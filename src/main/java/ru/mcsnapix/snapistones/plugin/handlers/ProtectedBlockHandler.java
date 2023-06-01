package ru.mcsnapix.snapistones.plugin.handlers;

import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.tr7zw.changeme.nbtapi.NBTItem;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.mcsnapix.snapistones.plugin.ReplacedList;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.api.ProtectedBlock;
import ru.mcsnapix.snapistones.plugin.api.SnapAPI;
import ru.mcsnapix.snapistones.plugin.api.SnapPlayer;
import ru.mcsnapix.snapistones.plugin.api.enums.ClickAction;
import ru.mcsnapix.snapistones.plugin.api.events.block.BlockInteractEvent;
import ru.mcsnapix.snapistones.plugin.api.events.region.RegionRemoveEvent;
import ru.mcsnapix.snapistones.plugin.database.Database;
import ru.mcsnapix.snapistones.plugin.modules.upgrade.UpgradeModule;
import ru.mcsnapix.snapistones.plugin.modules.upgrade.settings.UpgradeConfig;
import ru.mcsnapix.snapistones.plugin.serializers.ListSerializer;
import ru.mcsnapix.snapistones.plugin.utils.BlockUtil;
import ru.mcsnapix.snapistones.plugin.utils.FormatterUtil;
import ru.mcsnapix.snapistones.plugin.utils.RegionUtil;
import ru.mcsnapix.snapistones.plugin.xseries.XMaterial;

import java.util.List;

@RequiredArgsConstructor
public class ProtectedBlockHandler implements Listener {
    @NonNull
    private final SnapiStones plugin;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();

        if (!(action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }

        Location location = block.getLocation();
        if (!BlockUtil.isRegionProtectedBlock(location)) {
            return;
        }

        XMaterial item = XMaterial.matchXMaterial(block.getType());
        Player player = event.getPlayer();
        ProtectedBlock protectedBlock = BlockUtil.protectedBlock(item);

        RegionUtil regionUtil = new RegionUtil(player);
        ProtectedRegion region = regionUtil.getRegion(location);
        SnapPlayer snapPlayer = SnapAPI.player(player, region, protectedBlock);

        if (snapPlayer.hasPlayerInRegion()) {
            ClickAction clickAction = getClickAction(player, action);
            plugin.callEvent(new BlockInteractEvent(snapPlayer, clickAction, region, protectedBlock, location));
        }
    }

    private ClickAction getClickAction(Player player, Action action) {
        if (player.isSneaking()) {
            if (action == Action.LEFT_CLICK_BLOCK) {
                return ClickAction.LEFT_SHIFT;
            } else {
                return ClickAction.RIGHT_SHIFT;
            }
        } else {
            if (action == Action.LEFT_CLICK_BLOCK) {
                return ClickAction.LEFT;
            } else {
                return ClickAction.RIGHT;
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Location location = block.getLocation();

        if (!BlockUtil.isRegionProtectedBlock(location)) {
            return;
        }

        XMaterial item = XMaterial.matchXMaterial(block.getType());
        Player player = event.getPlayer();

        RegionUtil regionUtil = new RegionUtil(player);
        ProtectedBlock protectedBlock = BlockUtil.protectedBlock(item);
        ProtectedRegion region = regionUtil.getRegion(location);
        SnapPlayer snapPlayer = SnapAPI.player(player, region, protectedBlock);
        RegionManager regionManager = regionUtil.regionManager();

        if (!snapPlayer.hasOwnerPlayerInRegion()) {
            event.setCancelled(true);
            return;
        }

        event.setCancelled(true);
        block.setType(Material.AIR);
        // ! Module Upgrade
        Database database = new Database(region);
        List<String> effects = database.effects();
        int maxOwners = database.maxOwners();
        int maxMembers = database.maxMembers();

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

        plugin.callEvent(new RegionRemoveEvent(snapPlayer, region, protectedBlock, location));
        regionManager.removeRegion(region.getId());
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