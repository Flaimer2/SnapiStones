package ru.mcsnapix.snapistones.plugin.settings.config;

import space.arim.dazzleconf.annote.ConfDefault;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static space.arim.dazzleconf.annote.ConfDefault.DefaultString;
import static space.arim.dazzleconf.annote.ConfDefault.DefaultStrings;

public interface MainConfig {
    @SuppressWarnings("unused") // used by DazzleConf
    static Map<String, Integer> defaultRegionCount() {
        Map<String, Integer> regionCountMap = new HashMap<>();
        regionCountMap.put("default", 2);
        regionCountMap.put("premium", 3);
        regionCountMap.put("elite", 4);
        regionCountMap.put("legend", 5);
        regionCountMap.put("snapix", 6);
        return regionCountMap;
    }

    @DefaultStrings("world")
    List<String> enableWorld();

    @DefaultStrings("lobby")
    List<String> disableRegion();

    @DefaultString("invero")
    String menuOpenCommand();

    @ConfDefault.DefaultObject("defaultRegionCount")
    Map<String, Integer> regionCount();
}
