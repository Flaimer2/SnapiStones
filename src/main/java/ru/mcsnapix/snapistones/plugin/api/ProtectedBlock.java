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

@Accessors(fluent = true)
@Getter
public class ProtectedBlock {
    @NonNull
    private final XMaterial blockMaterial;
    @NonNull
    private final Location center;
    @NonNull
    private final BlockOption blockOption;
    @NonNull
    private final Database database;

    public ProtectedBlock(Region region) {
        database = region.database();
        center = LocationSerializer.deserialize(database.getColumnAsString(Column.LOCATION));
        blockMaterial = XMaterial.valueOf(database.getColumnAsString(Column.MATERIAL));
        blockOption = BlockUtil.getBlockOption(blockMaterial);
    }
}
