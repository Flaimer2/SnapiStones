package ru.mcsnapix.snapistones.plugin.modules.hologram.config;

import static space.arim.dazzleconf.annote.ConfDefault.DefaultObject;
import space.arim.dazzleconf.annote.SubSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface HologramConfig {
    @SuppressWarnings("unused") // used by DazzleConf
    static Map<String, HologramOption> defaultHolograms() {
        Map<String, HologramOption> hologramOptionMap = new HashMap<>();
        List<String> stringList = List.of(
                "§cОгромный приват",
                "",
                "§fВладелец: §a%region_owners%",
                "§fРазмер: §a67x67x67",
                "",
                "§aНажмите SHIFT + ПКМ, чтобы открыть меню"
        );
        hologramOptionMap.put(
                "RED_MUSHROOM_BLOCK",
                HologramOption.of(stringList, 3.0)
        );
        return hologramOptionMap;
    }

    @DefaultObject("defaultHolograms")
    Map<String, @SubSection HologramOption> blocks();
}
