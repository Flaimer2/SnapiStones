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
        return SnapPlayer.builder()
                .player(player)
                .placeholderParser(
                        PlaceholderParser.builder()
                                .player(player)
                                .build()
                ).build();
    }

    public SnapPlayer player(Player player, ProtectedRegion region) {
        return SnapPlayer.builder()
                .player(player)
                .region(region)
                .placeholderParser(
                        PlaceholderParser.builder()
                                .player(player)
                                .region(region)
                                .build()
                ).build();
    }

    public SnapPlayer player(Player player, ProtectedRegion region, ProtectedBlock protectedBlock) {
        return SnapPlayer.builder()
                .player(player)
                .region(region)
                .placeholderParser(
                        PlaceholderParser.builder()
                                .player(player)
                                .region(region)
                                .protectedBlock(protectedBlock)
                                .build()
                ).build();
    }

    public SnapPlayer player(Player player, ProtectedRegion region, ProtectedBlock protectedBlock, OfflinePlayer anotherPlayer) {
        return SnapPlayer.builder()
                .player(player)
                .region(region)
                .placeholderParser(
                        PlaceholderParser.builder()
                                .player(player)
                                .region(region)
                                .protectedBlock(protectedBlock)
                                .anotherPlayer(anotherPlayer)
                                .build()
                ).build();
    }

    public SnapPlayer player(Player player, ProtectedRegion region, String anotherName) {
        return SnapPlayer.builder()
                .player(player)
                .region(region)
                .placeholderParser(
                        PlaceholderParser.builder()
                                .player(player)
                                .region(region)
                                .anotherPlayer(Utils.offlinePlayer(anotherName))
                                .build()
                ).build();
    }
}
