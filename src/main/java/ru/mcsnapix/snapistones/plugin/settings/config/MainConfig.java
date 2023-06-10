package ru.mcsnapix.snapistones.plugin.settings.config;

import java.util.List;

import static space.arim.dazzleconf.annote.ConfDefault.DefaultStrings;

public interface MainConfig {
    @DefaultStrings("world")
    List<String> enableWorld();

    @DefaultStrings("lobby")
    List<String> disableRegion();
}
