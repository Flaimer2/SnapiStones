package ru.mcsnapix.snapistones.plugin.commands;

import co.aikar.commands.PaperCommandManager;
import org.bukkit.entity.Player;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.api.SnapApi;
import ru.mcsnapix.snapistones.plugin.api.region.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Commands {
    private final PaperCommandManager manager;

    public Commands() {
        manager = new PaperCommandManager(SnapiStones.get());
        manager.registerCommand(new RegionCommand());
        manager.registerCommand(new RegionAdminCommand());
        registerCommandCompletions();
    }

    private void registerCommandCompletions() {
        manager.getCommandCompletions().registerAsyncCompletion("playerinregion", c -> {
            Player player = c.getPlayer();
            List<String> players = new ArrayList<>();
            for (Region region : SnapApi.getRegionsByOwner(player.getName())) {
                players.addAll(region.members());
                players.addAll(region.owners());
            }
            players.remove(player.getName());

            return players;
        });

        manager.getCommandCompletions().registerAsyncCompletion("regionlistbyowner", c -> {
            Player player = c.getPlayer();

            return SnapApi.getRegionsByOwner(player.getName()).stream().map(Region::name).collect(Collectors.toList());
        });

        manager.getCommandCompletions().registerAsyncCompletion("regionlist", c -> SnapApi.getRegions().stream().map(Region::name).collect(Collectors.toList()));

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
