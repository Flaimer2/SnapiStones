package ru.mcsnapix.snapistones.plugin.api;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.bukkit.Location;
import ru.mcsnapix.snapistones.plugin.api.region.Region;
import ru.mcsnapix.snapistones.plugin.database.Column;
import ru.mcsnapix.snapistones.plugin.database.Database;
import ru.mcsnapix.snapistones.plugin.serializers.LocationSerializer;
import ru.mcsnapix.snapistones.plugin.settings.config.block.BlockOption;
import ru.mcsnapix.snapistones.plugin.util.BlockUtil;
import ru.mcsnapix.snapistones.plugin.xseries.XMaterial;

/**
 * It is a block that protects a certain radius
 */
@Accessors(fluent = true)
public class ProtectedBlock {
    /**
     * Material of the protected block.
     */
    @NonNull
    @Getter
    private final XMaterial blockMaterial;

    /**
     * Location of the center of the protected block.
     */
    @NonNull
    @Getter
    private final Location center;

    /**
     * Block option associated with a protected block.
     */
    @NonNull
    @Getter
    private final BlockOption blockOption;

    /**
     * Constructs a new ProtectedBlock instance using the provided region.
     *
     * @param region the region containing the protected block
     */
    public ProtectedBlock(Region region) {
        Database database = region.database();
        center = LocationSerializer.deserialise(database.getColumnAsString(Column.LOCATION));
        blockMaterial = XMaterial.valueOf(database.getColumnAsString(Column.MATERIAL));
        blockOption = BlockUtil.getBlockOption(blockMaterial);
    }
}
