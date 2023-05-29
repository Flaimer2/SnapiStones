package ru.mcsnapix.snapistones.plugin.modules.interfaces;

import ru.mcsnapix.snapistones.plugin.SnapiStones;

public interface IModule {
    void load(SnapiStones plugin);

    void reload();
}