package ru.mcsnapix.snapistones.plugin.settings.config.blocks;

public interface PlaceEffect {
    static PlaceEffect of(String name, int amount) {
        return new PlaceEffect() {
            @Override
            public String name() {
                return name;
            }

            @Override
            public int amount() {
                return amount;
            }
        };
    }

    String name();

    int amount();
}