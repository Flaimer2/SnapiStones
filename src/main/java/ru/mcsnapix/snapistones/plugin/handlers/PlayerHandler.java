package ru.mcsnapix.snapistones.plugin.handlers;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.api.ProtectedBlock;
import ru.mcsnapix.snapistones.plugin.api.SnapAPI;
import ru.mcsnapix.snapistones.plugin.api.SnapPlayer;
import ru.mcsnapix.snapistones.plugin.api.events.region.RegionEnterEvent;
import ru.mcsnapix.snapistones.plugin.api.events.region.RegionLeaveEvent;
import ru.mcsnapix.snapistones.plugin.utils.BlockUtil;
import ru.mcsnapix.snapistones.plugin.utils.RegionUtil;

import java.util.Map;
import java.util.WeakHashMap;

@RequiredArgsConstructor
public class PlayerHandler implements Listener {
    @NonNull
    private final SnapiStones plugin;
    private final Map<Player, ProtectedRegion> isOnABase = new WeakHashMap<>();

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMove(PlayerMoveEvent e) {
        checkEvents(e.getPlayer());
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        checkEvents(e.getPlayer());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        checkEvents(e.getEntity());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        checkEvents(e.getPlayer());
    }

    private void checkEvents(Player player) {
        if (player == null) return;

        RegionUtil regionUtil = new RegionUtil(player);
        Location location = player.getLocation();
        ProtectedRegion region = regionUtil.getRegion(location);
        ProtectedBlock protectedBlock = BlockUtil.protectedBlock(regionUtil.getCenter(region));
        SnapPlayer snapPlayer = SnapAPI.player(player, region, protectedBlock);

        if (isOnABase.containsKey(player)) {
            if (!snapPlayer.hasPlayerInRegion(region)) {
                plugin.callEvent(new RegionLeaveEvent(snapPlayer, isOnABase.get(player)));
                isOnABase.remove(player);
                return;
            }
            if (region != isOnABase.get(player)) {
                plugin.callEvent(new RegionLeaveEvent(snapPlayer, isOnABase.get(player)));
                plugin.callEvent(new RegionEnterEvent(snapPlayer, region));
                isOnABase.replace(player, region);
            }
        } else {
            if (snapPlayer.hasPlayerInRegion(region)) {
                plugin.callEvent(new RegionEnterEvent(snapPlayer, region));
                isOnABase.put(player, region);
            }
        }

    }
}
