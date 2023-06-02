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
        if (player == null) {
            return;
        }

        RegionUtil regionUtil = new RegionUtil(player);
        Location location = player.getLocation();

        ProtectedRegion currentRegion = regionUtil.getRegion(location);
        ProtectedRegion previousRegion = isOnABase.get(player);

        SnapPlayer snapPlayer = SnapAPI.player(player, currentRegion);

        if (previousRegion != null) {
            triggerRegionEvent(snapPlayer, previousRegion, location, EventType.LEAVE);
            triggerRegionEvent(snapPlayer, currentRegion, location, EventType.ENTER);
            isOnABase.replace(snapPlayer.player(), currentRegion);
        } else if (currentRegion != null) {
            triggerRegionEvent(snapPlayer, currentRegion, location, EventType.ENTER);
            isOnABase.put(snapPlayer.player(), currentRegion);
        } else {
            isOnABase.remove(snapPlayer.player());
        }
    }

    private void triggerRegionEvent(SnapPlayer snapPlayer, ProtectedRegion region, Location location, EventType eventType) {
        ProtectedBlock protectedBlock = BlockUtil.protectedBlock(location);
        if (eventType == EventType.ENTER) {
            plugin.callEvent(new RegionEnterEvent(region, snapPlayer, protectedBlock));
        } else if (eventType == EventType.LEAVE) {
            plugin.callEvent(new RegionLeaveEvent(region, snapPlayer, protectedBlock));
        }
    }

    private enum EventType {
        ENTER,
        LEAVE
    }
}
