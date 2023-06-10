package ru.mcsnapix.snapistones.plugin.settings.config;

import static space.arim.dazzleconf.annote.ConfDefault.DefaultString;

public interface MySQLConfig {
    @DefaultString("localhost:3306")
    String host();

    @DefaultString("server_global")
    String database();

    @DefaultString("root")
    String username();

    @DefaultString("root")
    String password();
}
