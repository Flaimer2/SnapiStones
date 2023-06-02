package ru.mcsnapix.snapistones.plugin.modules.flags.settings;

public interface FlagOptions {
    static FlagOptions of(String greeting, String farewell) {
        return new FlagOptions() {
            @Override
            public String greeting() {
                return greeting;
            }
            @Override
            public String farewell() {
                return farewell;
            }
        };
    }
    String greeting();
    String farewell();
}
