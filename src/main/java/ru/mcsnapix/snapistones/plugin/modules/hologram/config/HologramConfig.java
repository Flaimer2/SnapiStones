package ru.mcsnapix.snapistones.plugin.modules.hologram.config;

import space.arim.dazzleconf.annote.SubSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static space.arim.dazzleconf.annote.ConfDefault.DefaultObject;

public interface HologramConfig {
    @SuppressWarnings("unused") // used by DazzleConf
    static Map<String, HologramOption> defaultHolograms() {
        Map<String, HologramOption> hologramOptionMap = new HashMap<>();
        List<String> lines = List.of(
                "§cОгромный приват",
                "",
                "§fВладелец: §a%region_owners%",
                "§fРазмер: §a67x67x67",
                "",
                "§aНажмите SHIFT + ПКМ, чтобы открыть меню"
        );
        List<String> otherPlayerLines = List.of(
                "§cОгромный приват",
                "",
                "§fВладелец: §a%region_owners%",
                "§fРазмер: §a67x67x67"
        );
        hologramOptionMap.put(
                "RED_MUSHROOM_BLOCK",
                HologramOption.of(lines, otherPlayerLines, 3.0, 2.4, 64)
        );
        return hologramOptionMap;
    }

    @DefaultObject("defaultHolograms")
    Map<String, @SubSection HologramOption> blocks();
}
