package ru.mcsnapix.snapistones.plugin.handlers;

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
import ru.mcsnapix.snapistones.plugin.api.SnapApi;
import ru.mcsnapix.snapistones.plugin.api.events.region.RegionEnterEvent;
import ru.mcsnapix.snapistones.plugin.api.events.region.RegionLeaveEvent;
import ru.mcsnapix.snapistones.plugin.api.region.Region;

import java.util.Map;
import java.util.WeakHashMap;

@RequiredArgsConstructor
public class PlayerHandler implements Listener {
    private final SnapiStones plugin;
    private final Map<Player, Region> isOnABase = new WeakHashMap<>();

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

        Location location = player.getLocation();

        Region currentRegion = SnapApi.getRegion(location);
        Region previousRegion = isOnABase.get(player);

        if (previousRegion != null) {
            triggerRegionEvent(player, previousRegion, EventType.LEAVE);
            triggerRegionEvent(player, currentRegion, EventType.ENTER);
            isOnABase.replace(player, currentRegion);
        } else if (currentRegion != null) {
            triggerRegionEvent(player, currentRegion, EventType.ENTER);
            isOnABase.put(player, currentRegion);
        } else {
            isOnABase.remove(player);
        }
    }

    private void triggerRegionEvent(Player player, Region region, EventType eventType) {
        if (eventType == EventType.ENTER) {
            plugin.callEvent(new RegionEnterEvent(player, region));
        } else if (eventType == EventType.LEAVE) {
            plugin.callEvent(new RegionLeaveEvent(player, region));
        }
    }

    private enum EventType {
        ENTER,
        LEAVE
    }
}
