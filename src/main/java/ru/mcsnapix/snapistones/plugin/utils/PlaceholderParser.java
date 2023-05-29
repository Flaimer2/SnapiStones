package ru.mcsnapix.snapistones.plugin.utils;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.Builder;
import lombok.NonNull;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ru.mcsnapix.snapistones.plugin.api.ProtectedBlock;
import ru.mcsnapix.snapistones.plugin.database.Database;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public class PlaceholderParser {
    @Nullable
    private final Player player;
    @Nullable
    private final OfflinePlayer anotherPlayer;
    @Nullable
    private final ProtectedRegion region;
    @Nullable
    private final ProtectedBlock protectedBlock;

    private @NonNull String fillCommonPlaceholders(@NonNull String value) {
        if (player != null) {
            value = value.replace("%player_name%", player.getName());
            value = value.replace("%vault_prefix%", player.getName());
        }

        if (anotherPlayer != null) {
            value = value.replace("%another_player_name%", anotherPlayer.getName());
            value = value.replace("%another_vault_prefix%", anotherPlayer.getName());
        }

        if (region != null) {
            value = value.replace("%region_id%", region.getId());
            value = value.replace("%region_owners%", FormatterUtil.formatPlayerList(region.getOwners().getUniqueIds()));
            value = value.replace("%region_members%", FormatterUtil.formatPlayerList(region.getMembers().getUniqueIds()));
            value = value.replace("%region_owners_size%", Integer.toString(region.getOwners().size()));
            value = value.replace("%region_members_size%", Integer.toString(region.getMembers().size()));
            Database database = new Database(region);
            if (database.hasRegion()) {
                value = value.replace("%region_creation_date%", Database.getCreationRegionFormattedDate(id));
                value = value.replace("%region_max_owners%", Integer.toString(UpgradeDatabase.getMaxMembers(id, Column.MAX_OWNERS)));
                value = value.replace("%region_max_members%", Integer.toString(UpgradeDatabase.getMaxMembers(id, Column.MAX_MEMBERS)));
                value = value.replace("%region_has_home%", FormatterUtil.formatPlaceBoolean(Database.hasLocation(id)));
            }
        }

        if (protectedBlock != null) {
            value = value.replace("%region_size%", protectedBlock.formattedRadius());
        }

        return value;
    }

    public @NonNull List<String> parseStrings(@NonNull List<String> value) {
        return value.stream()
                .map(this::fillCommonPlaceholders)
                .collect(Collectors.toList());
    }

    public @NonNull String[] parseArrayString(@NonNull String[] value) {
        return Arrays.stream(value)
                .map(this::fillCommonPlaceholders)
                .toArray(String[]::new);
    }

    public @NonNull String parseString(@NonNull String value) {
        return fillCommonPlaceholders(value);
    }
}