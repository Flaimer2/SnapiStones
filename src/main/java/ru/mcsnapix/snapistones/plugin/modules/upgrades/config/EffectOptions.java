package ru.mcsnapix.snapistones.plugin.modules.upgrades.config;

public interface EffectOptions {
    static EffectOptions of(String effect, int level, String name) {
        return new EffectOptions() {
            @Override
            public String effect() {
                return effect;
            }

            @Override
            public int level() {
                return level;
            }

            @Override
            public String name() {
                return name;
            }
        };
    }

    String effect();

    int level();

    String name();

}
