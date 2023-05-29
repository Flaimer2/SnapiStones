package ru.mcsnapix.snapistones.plugin.settings.config.blocks;

public interface BlockOptions {
    static BlockOptions of(String symbol, int radius, String placeSound, String breakSound, PlaceEffect placeEffect) {
        return new BlockOptions() {

            @Override
            public String symbol() {
                return symbol;
            }

            @Override
            public int radius() {
                return radius;
            }

            @Override
            public String placeSound() {
                return placeSound;
            }

            @Override
            public String breakSound() {
                return breakSound;
            }

            @Override
            public PlaceEffect placeEffect() {
                return placeEffect;
            }
        };
    }

    String symbol();

    int radius();

    String placeSound();

    String breakSound();

    PlaceEffect placeEffect();
}
