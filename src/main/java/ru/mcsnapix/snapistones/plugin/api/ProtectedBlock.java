package ru.mcsnapix.snapistones.plugin.api;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import ru.mcsnapix.snapistones.plugin.xseries.XMaterial;

@Builder
@Accessors(fluent = true)
@Getter
public class ProtectedBlock {
    private final XMaterial material;
    private final String symbol;
    private final int radius;

    public String formattedRadius() {
        String formattedRadius = Integer.toString(radius * 2 + 1);
        return String.format("%sx%sx%s", formattedRadius, formattedRadius, formattedRadius);
    }
}