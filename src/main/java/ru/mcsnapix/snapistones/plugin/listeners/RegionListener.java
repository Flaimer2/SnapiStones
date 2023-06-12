package ru.mcsnapix.snapistones.plugin.listeners;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import ru.mcsnapix.snapistones.plugin.Placeholders;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.api.ProtectedBlock;
import ru.mcsnapix.snapistones.plugin.api.events.region.RegionCreateEvent;
import ru.mcsnapix.snapistones.plugin.api.events.region.RegionRemoveEvent;
import ru.mcsnapix.snapistones.plugin.api.region.Region;
import ru.mcsnapix.snapistones.plugin.api.region.RegionRegistry;
import ru.mcsnapix.snapistones.plugin.settings.config.block.BlockOption;
import ru.mcsnapix.snapistones.plugin.settings.message.Message;

public class RegionListener implements Listener {
    private final Message message;

    public RegionListener(SnapiStones plugin) {
        this.message = plugin.getMessage().data();
    }

    @EventHandler
    public void onRegionCreate(RegionCreateEvent event) {
        Player player = event.player();
        Region region = event.region();
        ProtectedBlock protectedBlock = region.protectedBlock();
        Location center = protectedBlock.center();
        BlockOption blockOption = protectedBlock.blockOption();
        Placeholders placeholders = new Placeholders(player, region);

        center.getNearbyPlayers(6).forEach(p ->
                p.playSound(center, Sound.valueOf(blockOption.placeSound()), 1.0F, 1.0F)
        );

        new ParticleBuilder(Particle.valueOf(blockOption.placeEffect().name()))
                .location(center)
                .count(blockOption.placeEffect().amount()).spawn();
        placeholders.sendMessage(message.protectedArea());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRegionRemove(RegionRemoveEvent event) {
        Player player = event.player();
        Region region = event.region();
        ProtectedBlock protectedBlock = region.protectedBlock();
        Location center = protectedBlock.center();
        BlockOption blockOption = protectedBlock.blockOption();
        Placeholders placeholders = new Placeholders(player, region);

        center.getNearbyPlayers(6).forEach(p ->
                p.playSound(center, Sound.valueOf(blockOption.breakSound()), 1.0F, 1.0F)
        );
        placeholders.sendMessage(message.protectedBlockBroken());
        RegionRegistry.get().removeRegion(region.name());
    }
}
