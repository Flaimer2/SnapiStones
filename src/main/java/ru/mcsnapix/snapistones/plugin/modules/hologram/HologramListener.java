package ru.mcsnapix.snapistones.plugin.modules.hologram;

import eu.decentsoftware.holograms.event.DecentHologramsReloadEvent;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import ru.mcsnapix.snapistones.plugin.api.SnapApi;
import ru.mcsnapix.snapistones.plugin.api.events.region.RegionCreateEvent;
import ru.mcsnapix.snapistones.plugin.api.events.region.RegionRemoveEvent;

@RequiredArgsConstructor
public class HologramListener implements Listener {
    private final HologramModule hologramModule;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRegionCreate(RegionCreateEvent event) {
        hologramModule.createHologram(event.region());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRegionRemove(RegionRemoveEvent event) {
        hologramModule.removeHologram(event.region());
    }

    @EventHandler
    public void onDecentHologramsReload(DecentHologramsReloadEvent event) {
        SnapApi.getRegions().forEach(hologramModule::createHologram);
    }
}
