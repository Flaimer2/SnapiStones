package ru.mcsnapix.snapistones.plugin;

import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.mcsnapix.snapistones.plugin.api.ProtectedBlock;
import ru.mcsnapix.snapistones.plugin.api.SnapApi;
import ru.mcsnapix.snapistones.plugin.api.region.Region;
import ru.mcsnapix.snapistones.plugin.modules.Modules;
import ru.mcsnapix.snapistones.plugin.modules.upgrades.config.UpgradeConfig;
import ru.mcsnapix.snapistones.plugin.util.FormatterUtil;

import java.util.ArrayList;
import java.util.List;

@Setter
public class Placeholders {
    private Player player;
    private Region region;
    private OfflinePlayer otherPlayer;

    public Placeholders(@NotNull Player player) {
        this.player = player;
    }

    public Placeholders(@NotNull Player player, Region region) {
        this.player = player;
        this.region = region;
    }

    public Placeholders(Region region) {
        this.region = region;
    }

    public String replacePlaceholders(String value) {
        UpgradeConfig upgradeConfig = null;
        Modules modules = SnapiStones.get().getModules();
        if (modules != null) {
            upgradeConfig = SnapiStones.get().getModules().upgrade().upgradeConfig().data();
        }

        if (player != null) {
            value = value.replace("%player_name%", player.getName());
            value = value.replace("%player_max_region_count%", Integer.toString(SnapApi.getMaxRegionCount(player)));
        }

        if (region != null) {
            value = value.replace("%region_id%", region.name());
            value = value.replace("%region_author%", region.author());
            value = value.replace("%region_owners%", FormatterUtil.formatPlayerList(region.owners()));
            value = value.replace("%region_members%", FormatterUtil.formatPlayerList(region.members()));
            value = value.replace("%region_owners_size%", Integer.toString(region.owners().size()));
            value = value.replace("%region_members_size%", Integer.toString(region.members().size()));
            value = value.replace("%region_creation_date%", Integer.toString(region.date()));
            if (upgradeConfig != null) {
                value = value.replace("%region_max_owners%", Integer.toString(upgradeConfig.limitOwner().get(region.maxOwners())));
                value = value.replace("%region_max_members%", Integer.toString(upgradeConfig.limitOwner().get(region.maxMembers())));
            }
            value = value.replace("%region_has_home%", FormatterUtil.formatPlaceBoolean(region.hasHomeLocation()));
            ProtectedBlock protectedBlock = region.protectedBlock();
            value = value.replace("%block_material%", protectedBlock.blockMaterialName());
            value = value.replace("%region_size%", protectedBlock.blockOption().formatRadius());
        }

        if (otherPlayer != null) {
            value = value.replace("%other_player_name%", otherPlayer.getName());
        }

        return value;
    }

    public void sendMessage(String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);

        player.sendMessage(replacePlaceholders(message));
    }

    public void sendMessage(List<String> messages) {
        messages.forEach(this::sendMessage);
    }

    public List<String> replacePlaceholders(List<String> value) {
        List<String> strings = new ArrayList<>();
        for (String s : value) {
            strings.add(replacePlaceholders(s));
        }
        return strings;
    }

    @SuppressWarnings("deprecation")
    public void setOtherPlayer(String name) {
        otherPlayer = Bukkit.getOfflinePlayer(name);
    }
}
