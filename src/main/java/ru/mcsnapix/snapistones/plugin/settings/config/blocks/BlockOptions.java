package ru.mcsnapix.snapistones.plugin.settings.config.blocks;

import space.arim.dazzleconf.annote.SubSection;

public interface BlockOptions {
    static BlockOptions of(String symbol, int radius, String placeSound, String breakSound, @SubSection PlaceEffect placeEffect) {
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
            public @SubSection PlaceEffect placeEffect() {
                return placeEffect;
            }
        };
    }
    String symbol();
    int radius();
    String placeSound();
    String breakSound();
    @SubSection PlaceEffect placeEffect();
}