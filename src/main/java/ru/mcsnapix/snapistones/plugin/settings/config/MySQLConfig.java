package ru.mcsnapix.snapistones.plugin.settings.config;

import space.arim.dazzleconf.annote.ConfDefault;

public interface MySQLConfig {
    @ConfDefault.DefaultString("localhost:3306")
    String host();

    @ConfDefault.DefaultString("server_global")
    String database();

    @ConfDefault.DefaultString("root")
    String username();

    @ConfDefault.DefaultString("root")
    String password();
}