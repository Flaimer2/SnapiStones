package ru.mcsnapix.snapistones.plugin.modules.menu;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ru.mcsnapix.snapistones.plugin.ClickAction;
import ru.mcsnapix.snapistones.plugin.Placeholders;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.api.events.block.BlockInteractEvent;
import ru.mcsnapix.snapistones.plugin.api.region.Region;

@RequiredArgsConstructor
public class MenuListener implements Listener {
    private final SnapiStones plugin;

    @EventHandler
    public void onBlockInteract(BlockInteractEvent event) {
        Player player = event.player();
        Region region = event.region();
        Placeholders placeholders = new Placeholders(player, region);

        if (event.action() != ClickAction.RIGHT_SHIFT) return;

        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), placeholders.replacePlaceholders(plugin.getMainConfig().data().menuOpenCommand()));
    }
}
