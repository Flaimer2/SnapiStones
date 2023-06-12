package ru.mcsnapix.snapistones.plugin.modules.flags;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ru.mcsnapix.snapistones.plugin.Placeholders;
import ru.mcsnapix.snapistones.plugin.api.ProtectedBlock;
import ru.mcsnapix.snapistones.plugin.api.events.region.RegionEnterEvent;
import ru.mcsnapix.snapistones.plugin.api.events.region.RegionLeaveEvent;
import ru.mcsnapix.snapistones.plugin.api.region.Region;
import ru.mcsnapix.snapistones.plugin.modules.flags.config.FlagConfig;

@AllArgsConstructor
public class FlagListener implements Listener {
    private final FlagConfig flagConfig;

    @EventHandler
    public void onRegionEnter(RegionEnterEvent event) {
        Player player = event.player();
        Region region = event.region();
        ProtectedBlock protectedBlock = region.protectedBlock();
        Placeholders placeholders = new Placeholders(player, region);
        placeholders.sendMessage(flagConfig.blocks().get(protectedBlock.blockMaterial().name()).greeting());
    }

    @EventHandler
    public void onRegionLeave(RegionLeaveEvent event) {
        Player player = event.player();
        Region region = event.region();
        ProtectedBlock protectedBlock = region.protectedBlock();
        Placeholders placeholders = new Placeholders(player, region);
        placeholders.sendMessage(flagConfig.blocks().get(protectedBlock.blockMaterial().name()).farewell());
    }
}
