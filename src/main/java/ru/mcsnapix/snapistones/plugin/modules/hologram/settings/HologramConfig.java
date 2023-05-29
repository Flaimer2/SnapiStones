package ru.mcsnapix.snapistones.plugin.modules.hologram.settings;

import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.SubSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface HologramConfig {
    static Map<String, HologramOptions> defaultHolograms() {
        Map<String, HologramOptions> hologramOptionsMap = new HashMap<>();
        List<String> stringList = List.of(
                "§cОгромный приват",
                "",
                "§fВладелец: §a%region_owners%",
                "§fРазмер: §a67x67x67",
                "",
                "§aНажмите SHIFT + ПКМ, чтобы открыть меню"
        );
        hologramOptionsMap.put(
                "RED_MUSHROOM_BLOCK",
                HologramOptions.of(stringList, 3.0)
        );
        return hologramOptionsMap;
    }

    @ConfDefault.DefaultObject("defaultHolograms")
    Map<String, @SubSection HologramOptions> blocks();
}
