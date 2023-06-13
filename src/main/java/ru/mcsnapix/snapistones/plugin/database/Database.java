package ru.mcsnapix.snapistones.plugin.database;

import co.aikar.idb.DB;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.Location;
import ru.mcsnapix.snapistones.plugin.serializers.ListSerializer;
import ru.mcsnapix.snapistones.plugin.serializers.LocationSerializer;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class Database {
    @NonNull
    private final String id;

    @SneakyThrows
    public void createRegion(String owner, Location location, String material) {
        DB.executeUpdate("INSERT INTO regions (region_name, owners_name, region_author, creation_date, block_location, block_material) VALUES (?,?,?,UNIX_TIMESTAMP(),?,?)",
                id, owner, owner, LocationSerializer.serialise(location), material);
    }

    @SneakyThrows
    public void removeRegion() {
        DB.executeUpdate("DELETE FROM regions WHERE `region_name` = ?",
                id);
    }

    @SneakyThrows
    public boolean hasRegion() {
        return DB.getFirstColumn("SELECT `region_name` FROM regions WHERE `region_name` = ?", id) != null;
    }

    @SneakyThrows
    public int getColumnAsInt(Column column) {
        return DB.getFirstColumn("SELECT " + column.getName() + " FROM regions WHERE `region_name` = ?", id);
    }

    @SneakyThrows
    public String getColumnAsString(Column column) {
        return DB.getFirstColumn("SELECT " + column.getName() + " FROM regions WHERE `region_name` = ?", id);
    }

    public List<String> getColumnAsList(Column column) {
        String serializedList = getColumnAsString(column);
        if (serializedList == null) return Collections.emptyList();
        return ListSerializer.deserialise(serializedList);
    }

    @SneakyThrows
    public void updateColumn(Column column, String value) {
        DB.executeUpdate("UPDATE regions SET " + column.getName() + " = ? WHERE region_name = ?", value, id);
    }

    @SneakyThrows
    public void updateColumn(Column column, int value) {
        DB.executeUpdate("UPDATE regions SET " + column.getName() + " = ? WHERE region_name = ?", value, id);
    }
}
