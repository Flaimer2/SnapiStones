package ru.mcsnapix.snapistones.plugin.modules.hologram;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import ru.mcsnapix.snapistones.plugin.Placeholders;
import ru.mcsnapix.snapistones.plugin.api.ProtectedBlock;
import ru.mcsnapix.snapistones.plugin.api.events.region.RegionCreateEvent;
import ru.mcsnapix.snapistones.plugin.api.events.region.RegionRemoveEvent;
import ru.mcsnapix.snapistones.plugin.api.region.Region;
import ru.mcsnapix.snapistones.plugin.modules.hologram.config.HologramConfig;
import ru.mcsnapix.snapistones.plugin.modules.hologram.config.HologramOption;

@RequiredArgsConstructor
public class HologramListener implements Listener {
    private final HologramManager hologramManager = DecentHologramsAPI.get().getHologramManager();
    private final HologramConfig config;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRegionCreate(RegionCreateEvent event) {
        Region region = event.region();
        Placeholders placeholders = new Placeholders(region);
        ProtectedBlock protectedBlock = region.protectedBlock();
        String id = region.name();
        String blockMaterialName = protectedBlock.blockMaterialName();
        HologramOption hologramOption = config.blocks().get(blockMaterialName);

        if (hologramOption == null) return;

        Hologram hologram = DHAPI.createHologram(id, protectedBlock.center().add(0.5, hologramOption.height(), 0.5), placeholders.replacePlaceholders(hologramOption.lines()));

        hologram.showAll();
        hologram.save();

        hologramManager.registerHologram(hologram);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRegionCreate(RegionRemoveEvent event) {
        String id = event.region().name();
        Hologram hologram = hologramManager.getHologram(id);

        hologram.delete();
        hologramManager.removeHologram(id);
    }
}
