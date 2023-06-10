package ru.mcsnapix.snapistones.plugin.modules.flags;

import lombok.AllArgsConstructor;
import org.bukkit.event.Listener;
import ru.mcsnapix.snapistones.plugin.modules.flags.config.FlagConfig;

@AllArgsConstructor
public class FlagListener implements Listener {
    private final FlagConfig flagConfig;

    public void onRegionEnter(RegionEnterEvent event) {
        SnapPlayer snapPlayer = SnapAPI.player(event.player().player(), event.region());
        String greeting = flagConfig.blocks().get();
        snapPlayer.sendMessage(flagConfig.blocks().get());
    }
}
