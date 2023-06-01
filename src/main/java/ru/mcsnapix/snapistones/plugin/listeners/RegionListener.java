package ru.mcsnapix.snapistones.plugin.listeners;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.api.ProtectedBlock;
import ru.mcsnapix.snapistones.plugin.api.SnapPlayer;
import ru.mcsnapix.snapistones.plugin.api.events.region.RegionCreateEvent;
import ru.mcsnapix.snapistones.plugin.api.events.region.RegionRemoveEvent;
import ru.mcsnapix.snapistones.plugin.settings.config.blocks.BlockOptions;
import ru.mcsnapix.snapistones.plugin.settings.message.Message;
import ru.mcsnapix.snapistones.plugin.utils.BlockUtil;

public class RegionListener implements Listener {
    private final Message message;

    public RegionListener(SnapiStones plugin) {
        this.message = plugin.message().data();
    }

    @EventHandler
    public void onRegionCreate(RegionCreateEvent event) {
        SnapPlayer player = event.player();
        ProtectedBlock block = event.protectedBlock();
        Location location = event.location();
        BlockOptions blockOptions = BlockUtil.blockOptions(block.material());

        location.getNearbyPlayers(6).forEach(p ->
                p.playSound(location, Sound
                                .valueOf(blockOptions.placeSound()),
                        1.0F, 1.0F));
        new ParticleBuilder(Particle.valueOf(blockOptions.placeEffect().name()))
                .location(location)
                .count(blockOptions.placeEffect().amount()).spawn();
        player.sendMessage(message.protectedArea());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRegionRemove(RegionRemoveEvent event) {
        SnapPlayer player = event.player();
        ProtectedBlock block = event.protectedBlock();
        Location location = event.location();
        BlockOptions blockOptions = BlockUtil.blockOptions(block.material());

        location
                .getNearbyPlayers(6)
                .forEach(p ->
                        p.playSound(location,
                                Sound.valueOf(blockOptions.breakSound()),
                                1.0F, 1.0F
                        )
                );
        player.sendMessage(message.protectedBlockBroken());
    }
}