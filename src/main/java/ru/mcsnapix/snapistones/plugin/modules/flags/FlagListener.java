package ru.mcsnapix.snapistones.plugin.modules.flags;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.Listener;
import ru.mcsnapix.snapistones.plugin.api.SnapAPI;
import ru.mcsnapix.snapistones.plugin.api.SnapPlayer;
import ru.mcsnapix.snapistones.plugin.api.events.region.RegionEnterEvent;
import ru.mcsnapix.snapistones.plugin.modules.flags.settings.FlagConfig;

@RequiredArgsConstructor
public class FlagListener implements Listener {
    private final FlagConfig flagConfig;

    public void onRegionEnter(RegionEnterEvent event) {
        SnapPlayer snapPlayer = SnapAPI.player(event.player().player(), event.region());
        String greeting = flagConfig.blocks().get();
        snapPlayer.sendMessage(flagConfig.blocks().get());
    }
}
