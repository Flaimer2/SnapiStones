package ru.mcsnapix.snapistones.plugin.modules.hologram.settings;

import java.util.List;

public interface HologramOptions {
    static HologramOptions of(List<String> lines, double height) {
        return new HologramOptions() {

            @Override
            public List<String> lines() {
                return lines;
            }

            @Override
            public double height() {
                return height;
            }
        };
    }

    List<String> lines();

    double height();
}
