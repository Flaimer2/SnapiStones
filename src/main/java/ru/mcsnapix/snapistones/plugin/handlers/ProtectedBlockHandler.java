package ru.mcsnapix.snapistones.plugin.handlers;

import com.sk89q.worldguard.protection.managers.RegionManager;
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
import ru.mcsnapix.snapistones.plugin.ClickAction;
import ru.mcsnapix.snapistones.plugin.ReplacedList;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.api.SnapApi;
import ru.mcsnapix.snapistones.plugin.api.events.block.BlockInteractEvent;
import ru.mcsnapix.snapistones.plugin.api.events.region.RegionRemoveEvent;
import ru.mcsnapix.snapistones.plugin.api.region.Region;
import ru.mcsnapix.snapistones.plugin.database.Database;
import ru.mcsnapix.snapistones.plugin.modules.Modules;
import ru.mcsnapix.snapistones.plugin.modules.upgrades.UpgradeModule;
import ru.mcsnapix.snapistones.plugin.modules.upgrades.config.UpgradeConfig;
import ru.mcsnapix.snapistones.plugin.serializers.ListSerializer;
import ru.mcsnapix.snapistones.plugin.util.FormatterUtil;
import ru.mcsnapix.snapistones.plugin.util.WGRegionUtil;
import ru.mcsnapix.snapistones.plugin.xseries.XMaterial;

import java.util.ArrayList;
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

        if (!SnapApi.isProtectedBlock(location)) return;

        Player player = event.getPlayer();
        if (player == null) return;
        if (!region.hasPlayerInRegion(player.getName())) return;

        ClickAction clickAction = ClickAction.getClickAction(player, action);
        plugin.callEvent(new BlockInteractEvent(player, clickAction, region));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block == null) return;

        XMaterial item = XMaterial.matchXMaterial(block.getType());

        Location location = block.getLocation();
        Region region = SnapApi.getRegion(location);

        if (!SnapApi.isProtectedBlock(location)) return;

        RegionManager regionManager = WGRegionUtil.getRegionManager(location.getWorld());
        event.setCancelled(true);

        Player player = event.getPlayer();
        if (player == null) return;
        if (!region.hasOwnerInRegion(player.getName())) return;

        block.setType(Material.AIR);

        Modules modules = plugin.getModules();

        // ! Module Upgrade
        List<String> effects = region.effects();
        int maxOwners = region.maxOwners();
        int maxMembers = region.maxMembers();

        UpgradeModule upgradeModule = modules.upgrade();
        UpgradeConfig upgradeConfig = upgradeModule.upgradeConfig().data();
        var itemConfig = upgradeConfig.item();

        ItemStack itemDrop = item.parseItem();
        assert itemDrop != null;
        ItemMeta itemMeta = itemDrop.getItemMeta();
        itemMeta.setDisplayName(itemConfig.name());

        ReplacedList lore = new ReplacedList(itemConfig.lore());

        if (!effects.isEmpty()) {
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

        NBTItem nbtItem = new NBTItem(itemDrop);

        if (!effects.isEmpty()) {
            nbtItem.setString("effect", ListSerializer.serialise(effects));
        }
        nbtItem.setString("maxOwner", Integer.toString(maxOwners));
        nbtItem.setString("maxMember", Integer.toString(maxMembers));

        block.getWorld().dropItem(block.getLocation(), nbtItem.getItem());

        plugin.callEvent(new RegionRemoveEvent(player, region));
        regionManager.removeRegion(region.name());
        Database database = new Database(region.name());
        database.removeRegion();
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        List<Block> blocks = event.blockList();

        event.blockList().removeAll(getRemoveBlocks(blocks));
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        List<Block> blocks = event.blockList();

        event.blockList().removeAll(getRemoveBlocks(blocks));
    }

    public List<Block> getRemoveBlocks(List<Block> blocks) {
        List<Block> removeBlocks = new ArrayList<>();

        for (Block block : blocks) {
            Location location = block.getLocation();
            if (SnapApi.isProtectedBlock(location)) removeBlocks.add(block);
        }

        return removeBlocks;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPistonExtend(BlockPistonExtendEvent e) {
        cancelEventIfProtectedBlocksPresent(e.getBlocks(), e);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPistonRetract(BlockPistonRetractEvent e) {
        cancelEventIfProtectedBlocksPresent(e.getBlocks(), e);
    }

    private void cancelEventIfProtectedBlocksPresent(List<Block> pushedBlocks, BlockPistonEvent event) {
        for (Block block : pushedBlocks) {
            Location location = block.getLocation();

            if (SnapApi.isProtectedBlock(location)) {
                event.setCancelled(true);
            }
        }
    }
}
