package ru.mcsnapix.snapistones.plugin.modules.hologram.config;

import java.util.List;

public interface HologramOption {
    static HologramOption of(List<String> lines, List<String> otherPlayerLines, double height, double otherPlayerHeight, int viewDistance) {
        return new HologramOption() {
            @Override
            public List<String> lines() {
                return lines;
            }

            @Override
            public List<String> otherPlayerLines() {
                return otherPlayerLines;
            }

            @Override
            public double height() {
                return height;
            }

            @Override
            public double otherPlayerHeight() {
                return otherPlayerHeight;
            }

            @Override
            public int viewDistance() {
                return viewDistance;
            }
        };
    }

    List<String> lines();

    List<String> otherPlayerLines();

    double height();

    double otherPlayerHeight();

    int viewDistance();
}
