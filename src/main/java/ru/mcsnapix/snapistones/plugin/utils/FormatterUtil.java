package ru.mcsnapix.snapistones.plugin.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import ru.mcsnapix.snapistones.plugin.xseries.XMaterial;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@UtilityClass
public class FormatterUtil {
    public String formatPlayerList(Set<UUID> uuids) {
        List<String> playerNames = new ArrayList<>();

        for (UUID uuid : uuids) {
            String playerName = Bukkit.getOfflinePlayer(uuid).getName();
            if (playerName != null) {
                playerNames.add("§a" + playerName + "§f");
            }
        }

        if (playerNames.isEmpty()) {
            return "Нет игроков";
        }

        return String.join(", ", playerNames);
    }

    public List<String> formatList(List<String> list, String format) {
        if (list == null) return null;

        List<String> formatList = new ArrayList<>();

        for (String s : list) {
            formatList.add(format.replace("%name%", s));
        }

        return formatList;
    }

    public String formatPlaceBoolean(boolean value) {
        return value ? "§aустановлена" : "§cне установлена";
    }

    public String formatLocation(Location location) {
        return "x=" + toInt(location.getBlockX()) +
                ", y=" + toInt(location.getBlockY()) +
                ", z=" + toInt(location.getBlockZ());
    }

    private int toInt(int x) {
        return Integer.parseInt(String.valueOf(x));
    }

    public String formatItem(XMaterial item) {
        return item.name();
    }
}
