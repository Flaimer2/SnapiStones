package ru.mcsnapix.snapistones.plugin.modules.hologram;

import eu.decentsoftware.holograms.event.DecentHologramsReloadEvent;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.api.SnapApi;
import ru.mcsnapix.snapistones.plugin.api.events.region.RegionAddPlayerEvent;
import ru.mcsnapix.snapistones.plugin.api.events.region.RegionCreateEvent;
import ru.mcsnapix.snapistones.plugin.api.events.region.RegionRemoveEvent;
import ru.mcsnapix.snapistones.plugin.api.events.region.RegionRemovePlayerEvent;
import ru.mcsnapix.snapistones.plugin.api.region.Region;

@AllArgsConstructor
public class HologramListener implements Listener {
    private final HologramModule hologramModule;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRegionCreate(RegionCreateEvent event) {
        Region region = event.region();

        hologramModule.createHologram(region);
        SnapiStones.get().getMorePaperLib().scheduling().asyncScheduler().run(() -> Bukkit.getOnlinePlayers().forEach(p -> hologramModule.updateHologramForPlayer(region, p)));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRegionRemove(RegionRemoveEvent event) {
        Region region = event.region();
        hologramModule.removeHologram(region);
    }

    @EventHandler
    public void onDecentHologramsReload(DecentHologramsReloadEvent event) {
        SnapApi.getRegions().forEach(hologramModule::createHologram);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for (Region region : SnapApi.getRegions()) {
            hologramModule.updateHologramForPlayer(region, player);
        }
    }

    @EventHandler
    public void onRegionAddPlayer(RegionAddPlayerEvent event) {
        SnapiStones.get().getMorePaperLib().scheduling().asyncScheduler().run(() -> hologramModule.updateHologramForPlayer(event.region(), event.player()));
    }

    @EventHandler
    public void onRegionRemovePlayer(RegionRemovePlayerEvent event) {
        SnapiStones.get().getMorePaperLib().scheduling().asyncScheduler().run(() -> hologramModule.updateHologramForPlayer(event.region(), event.player()));
    }
}
