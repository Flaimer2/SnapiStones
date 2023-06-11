package ru.mcsnapix.snapistones.plugin.commands;

import co.aikar.commands.PaperCommandManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.entity.Player;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.api.SnapApi;
import ru.mcsnapix.snapistones.plugin.api.region.Region;

import java.util.stream.Collectors;

public class Commands {
    private final SnapiStones plugin = SnapiStones.get();
    private final PaperCommandManager manager;

    public Commands() {
        manager = new PaperCommandManager(plugin);
        manager.registerCommand(new RegionCommand());
        registerCommandCompletions();
    }

    private void registerCommandCompletions() {
        manager.getCommandCompletions().registerAsyncCompletion("regionlistbyowner", c -> {
            Player player = c.getPlayer();

            return SnapApi.getRegionsByOwner(player.getName()).stream().map(Region::name).collect(Collectors.toList());
        });

        manager.getCommandCompletions().registerAsyncCompletion("regionlist", c -> {
            Player player = c.getPlayer();

            return SnapApi.getRegions().stream().map(Region::name).collect(Collectors.toList());
        });

        manager.getCommandCompletions().registerAsyncCompletion("regionlistbyplayer", c -> {
            Player player = c.getPlayer();

            return SnapApi.getRegionsByPlayer(player.getName()).stream().map(Region::name).collect(Collectors.toList());
        });

        manager.getCommandCompletions().registerAsyncCompletion("regionhomelistbyplayer", c -> {
            Player player = c.getPlayer();

            return SnapApi.getRegionsByPlayer(player.getName()).stream().filter(Region::hasHomeLocation).map(Region::name).collect(Collectors.toList());
        });
    }
}
