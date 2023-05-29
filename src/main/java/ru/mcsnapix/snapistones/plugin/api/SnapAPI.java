package ru.mcsnapix.snapistones.plugin.api;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.experimental.UtilityClass;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import ru.mcsnapix.snapistones.plugin.utils.PlaceholderParser;
import ru.mcsnapix.snapistones.plugin.utils.Utils;

@UtilityClass
public class SnapAPI {
    public SnapPlayer player(Player player) {
        return new SnapPlayer(player, PlaceholderParser.builder()
                .player(player)
                .build());
    }

    public SnapPlayer player(Player player, ProtectedRegion region) {
        return new SnapPlayer(player, region, PlaceholderParser.builder()
                .player(player)
                .region(region)
                .build());
    }

    public SnapPlayer player(Player player, ProtectedRegion region, ProtectedBlock protectedBlock) {
        return new SnapPlayer(player, region, PlaceholderParser.builder()
                .player(player)
                .region(region)
                .protectedBlock(protectedBlock)
                .build());
    }

    public SnapPlayer player(Player player, ProtectedRegion region, ProtectedBlock protectedBlock, String anotherName) {
        return new SnapPlayer(player, region, PlaceholderParser.builder()
                .player(player)
                .region(region)
                .protectedBlock(protectedBlock)
                .anotherPlayer(Utils.offlinePlayer(anotherName))
                .build());
    }

    public SnapPlayer player(Player player, ProtectedRegion region, String anotherName) {
        return new SnapPlayer(player, region, PlaceholderParser.builder()
                .player(player)
                .region(region)
                .anotherPlayer(Utils.offlinePlayer(anotherName))
                .build());
    }
}
