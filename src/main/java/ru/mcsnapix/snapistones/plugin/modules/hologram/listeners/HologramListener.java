package ru.mcsnapix.snapistones.plugin.modules.hologram.listeners;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import ru.mcsnapix.snapistones.plugin.api.SnapPlayer;
import ru.mcsnapix.snapistones.plugin.api.events.region.RegionCreateEvent;
import ru.mcsnapix.snapistones.plugin.api.events.region.RegionRemoveEvent;
import ru.mcsnapix.snapistones.plugin.modules.hologram.HologramModule;
import ru.mcsnapix.snapistones.plugin.modules.hologram.settings.HologramConfig;
import ru.mcsnapix.snapistones.plugin.modules.hologram.settings.HologramOptions;
import ru.mcsnapix.snapistones.plugin.utils.FormatterUtil;

@RequiredArgsConstructor
public class HologramListener implements Listener {
    private final HologramModule hologramModule;
    private final HologramManager hm = DecentHologramsAPI.get().getHologramManager();
    private final HologramConfig hologramConfig = hologramModule.hologramConfig().data();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRegionCreate(RegionCreateEvent event) {
        SnapPlayer player = event.player();

        String id = event.region().getId();
        String nameBlock = FormatterUtil.formatItem(event.protectedBlock().material());
        HologramOptions hologramOptions = hologramConfig.blocks().get(nameBlock);

        Hologram hologram = DHAPI.createHologram(
                id,
                event.location().add(
                        0.5,
                        hologramOptions.height(),
                        0.5),
                player.placeholderParser().parseStrings(hologramOptions.lines())
        );

        hologram.showAll();
        hologram.save();
        hm.registerHologram(hologram);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRegionCreate(RegionRemoveEvent event) {
        String id = event.region().getId();
        Hologram hologram = hm.getHologram(id);

        hologram.delete();
        hm.removeHologram(id);
    }
}
