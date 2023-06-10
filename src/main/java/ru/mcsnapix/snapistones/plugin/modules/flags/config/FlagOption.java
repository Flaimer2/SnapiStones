package ru.mcsnapix.snapistones.plugin.modules.flags.config;

public interface FlagOption {
    static FlagOption of(String greeting, String farewell) {
        return new FlagOption() {
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
