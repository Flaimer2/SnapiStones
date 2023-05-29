package ru.mcsnapix.snapistones.plugin.settings.config;

import space.arim.dazzleconf.annote.ConfDefault;

import java.util.List;

public interface MainConfig {
    @ConfDefault.DefaultStrings("world")
    List<String> enableWorld();

    @ConfDefault.DefaultStrings("lobby")
    List<String> disableRegion();
}