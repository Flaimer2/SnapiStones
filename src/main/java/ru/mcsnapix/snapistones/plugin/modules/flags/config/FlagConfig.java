package ru.mcsnapix.snapistones.plugin.modules.flags.config;

import space.arim.dazzleconf.annote.ConfDefault.DefaultObject;
import space.arim.dazzleconf.annote.SubSection;

import java.util.HashMap;
import java.util.Map;

public interface FlagConfig {
    @SuppressWarnings("unused") // used by DazzleConf
    static Map<String, FlagOption> defaultFlags() {
        Map<String, FlagOption> flagOptionsMap = new HashMap<>();
        flagOptionsMap.put(
                "RED_MUSHROOM_BLOCK",
                FlagOption.of("&fВы &aвошли &fв регион игрока &a%other_player_name%", "")
        );
        return flagOptionsMap;
    }

    @DefaultObject("defaultFlags")
    Map<String, @SubSection FlagOption> blocks();
}
