package ru.mcsnapix.snapistones.plugin.placeholder;

import lombok.NonNull;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.mcsnapix.snapistones.plugin.api.ProtectedBlock;
import ru.mcsnapix.snapistones.plugin.api.region.Region;
import ru.mcsnapix.snapistones.plugin.util.FormatterUtil;

public class PlaceholderManager {
    @NonNull
    private final Player player;
    private Region region;
    private OfflinePlayer otherPlayer;

    public PlaceholderManager(@NotNull Player player) {
        this.player = player;
    }

    public PlaceholderManager(@NotNull Player player, Region region) {
        this.player = player;
        this.region = region;
    }

    public PlaceholderManager(@NotNull Player player, Region region, OfflinePlayer otherPlayer) {
        this.player = player;
        this.region = region;
        this.otherPlayer = otherPlayer;
    }

    public String replacePlaceholders(String value) {
        value = value.replace("%player_name%", player.getName());

        if (region != null) {
            value = value.replace("%region_id%", region.getId());
            value = value.replace("%region_owners%", FormatterUtil.formatPlayerList(region.owners()));
            value = value.replace("%region_members%", FormatterUtil.formatPlayerList(region.members()));
            value = value.replace("%region_owners_size%", Integer.toString(region.owners().size()));
            value = value.replace("%region_members_size%", Integer.toString(region.members().size()));
            value = value.replace("%region_creation_date%", Integer.toString(region.getDate()));
            value = value.replace("%region_max_owners%", Integer.toString(region.getMaxOwners()));
            value = value.replace("%region_max_members%", Integer.toString(region.getMaxMembers()));
            value = value.replace("%region_has_home%", FormatterUtil.formatPlaceBoolean(region.hasHomeLocation()));
            ProtectedBlock protectedBlock = region.getProtectedBlock();
            value = value.replace("%block_material%", protectedBlock.getBlockMaterial().name());
            value = value.replace("%region_size%", protectedBlock.getBlockOption().formatRadius());
        }

        if (otherPlayer != null) {
            value = value.replace("%other_player_name%", otherPlayer.getName());
        }

        return value;
    }
}
