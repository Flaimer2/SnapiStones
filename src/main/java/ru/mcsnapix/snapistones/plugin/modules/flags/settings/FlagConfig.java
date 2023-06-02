package ru.mcsnapix.snapistones.plugin.modules.flags.settings;

import ru.mcsnapix.snapistones.plugin.modules.hologram.settings.HologramOptions;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.SubSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface FlagConfig {
    static Map<String, FlagOptions> defaultFlags() {
        Map<String, FlagOptions> flagOptionsMap = new HashMap<>();
        flagOptionsMap.put(
                "RED_MUSHROOM_BLOCK",
                FlagOptions.of("&fВы &aвошли &fв регион игрока &a%other_player_name%", "")
        );
        return flagOptionsMap;
    }

    @ConfDefault.DefaultObject("defaultFlags")
    Map<String, @SubSection FlagOptions> blocks();
}
