package ru.mcsnapix.snapistones.plugin.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.bukkit.util.NumberConversions.toInt;

@UtilityClass
public class FormatterUtil {
    public String formatPlayerList(List<String> strings) {
        List<String> names = new ArrayList<>();

        for (String name : strings) {
            if (name != null) {
                names.add(name);
            }
        }

        if (names.isEmpty()) {
            return "Нет игроков";
        }

        return String.join(", ", names);
    }

    public List<String> formatList(List<String> list, String format) {
        if (list == null || list.isEmpty()) return Collections.emptyList();

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
        return "x=" + location.getBlockX() +
                ", y=" + location.getBlockY() +
                ", z=" + toInt(location.getBlockZ());
    }
}
