package ru.mcsnapix.snapistones.plugin.modules.hologram.config;

import java.util.List;

public interface HologramOption {
    static HologramOption of(List<String> lines, double height) {
        return new HologramOption() {
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
