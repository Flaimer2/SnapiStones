package ru.mcsnapix.snapistones.plugin.settings.config.block;

import space.arim.dazzleconf.annote.SubSection;

public interface BlockOption {
    static BlockOption of(String symbol, int radius, String placeSound, String breakSound, boolean alwaysCreateRegion, @SubSection PlaceEffect placeEffect) {
        return new BlockOption() {
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
            public boolean alwaysCreateRegion() {
                return alwaysCreateRegion;
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

    boolean alwaysCreateRegion();

    @SubSection PlaceEffect placeEffect();

    default String formatRadius() {
        String formattedRadius = Integer.toString(radius() * 2 + 1);
        return String.format("%sx%sx%s", formattedRadius, formattedRadius, formattedRadius);
    }
}
