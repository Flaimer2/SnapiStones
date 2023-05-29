package ru.mcsnapix.snapistones.plugin.commands;

import co.aikar.commands.PaperCommandManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.entity.Player;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.api.SnapAPI;
import ru.mcsnapix.snapistones.plugin.api.SnapPlayer;
import ru.mcsnapix.snapistones.plugin.utils.RegionUtil;

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
        manager.getCommandCompletions().registerAsyncCompletion("myregionlistbyowner", c -> {
            Player player = c.getPlayer();
            SnapPlayer snapPlayer = SnapAPI.player(player);

            return snapPlayer.ownerRegions().stream().map(ProtectedRegion::getId).collect(Collectors.toList());
        });

        manager.getCommandCompletions().registerAsyncCompletion("regionlist", c -> {
            Player player = c.getPlayer();
            RegionUtil regionUtil = new RegionUtil(player.getWorld());

            return regionUtil.getRegions().stream().map(ProtectedRegion::getId).collect(Collectors.toList());
        });

        manager.getCommandCompletions().registerAsyncCompletion("myregionlistbymember", c -> {
            Player player = c.getPlayer();
            SnapPlayer snapPlayer = SnapAPI.player(player);

            return snapPlayer.memberRegions().stream().map(ProtectedRegion::getId).collect(Collectors.toList());
        });
    }
}