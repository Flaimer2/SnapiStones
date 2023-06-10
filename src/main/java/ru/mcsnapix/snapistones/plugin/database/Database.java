package ru.mcsnapix.snapistones.plugin.database;

import co.aikar.idb.DB;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.Location;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.serializers.ListSerializer;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class Database {
    private final SnapiStones plugin = SnapiStones.get();
    @NonNull
    private final String id;

    public void createRegion(String owner, Location location, String material) {
        DB.executeUpdateAsync("INSERT INTO regions (region_name, owners_name, creation_date, block_location, block_material) VALUES (?,?,UNIX_TIMESTAMP(),?)",
                id, owner, location, material);
    }

    public void removeRegion() {
        DB.executeUpdateAsync("DELETE FROM regions WHERE `region_name` = ?",
                id);
    }

    @SneakyThrows
    public boolean hasRegion() {
        return DB.getFirstColumn("SELECT `region_name` FROM regions WHERE `region_name` = ?", id) != null;
    }

    @SneakyThrows
    public int getColumnAsInt(Column column) {
        return DB.getFirstColumn("SELECT " + column.getName() + " FROM regions WHERE `region_name` = ?", regionId);
    }

    @SneakyThrows
    public String getColumnAsString(Column column) {
        return DB.getFirstColumn("SELECT " + column.getName() + " FROM regions WHERE `region_name` = ?", regionId);
    }

    public List<String> getColumnAsList(Column column) {
        String serializedList = getColumnAsString(column);
        if (serializedList == null) return Collections.emptyList();
        return ListSerializer.deserialize(serializedList);
    }

    @SneakyThrows
    public void updateColumn(Column column, String value) {
        DB.executeUpdateAsync("UPDATE regions SET " + column.getName() + " = ? WHERE region_name = ?", value, regionId);
    }

    @SneakyThrows
    public void updateColumn(Column column, int value) {
        DB.executeUpdateAsync("UPDATE regions SET " + column.getName() + " = ? WHERE region_name = ?", value, regionId);
    }
}
