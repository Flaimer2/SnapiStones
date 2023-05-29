package ru.mcsnapix.snapistones.plugin.database;

import co.aikar.idb.DB;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.Location;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.serializers.ListSerializer;
import ru.mcsnapix.snapistones.plugin.serializers.LocationSerializer;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Database {
    private final SnapiStones plugin = SnapiStones.get();
    @NonNull
    private ProtectedRegion region;
    private final String regionId = region.getId();

    public void createRegion(String owner) {
        DB.executeUpdateAsync("INSERT INTO regions (region_name, owners_name, creation_date) VALUES (?,?,UNIX_TIMESTAMP())",
                regionId, owner);
    }

    public void removeRegion() {
        DB.executeUpdateAsync("DELETE FROM regions WHERE `region_name` = ?",
                regionId);
    }

    @SneakyThrows
    public boolean hasRegion() {
        return DB.getFirstColumn("SELECT `region_name` FROM regions WHERE `region_name` = ?", regionId) != null;
    }

    @SneakyThrows
    private int getColumnAsInt(Column column) {
        return DB.getFirstColumn("SELECT " + column.column() + " FROM regions WHERE `region_name` = ?", column.column(), regionId);
    }

    @SneakyThrows
    private String getColumnAsString(Column column) {
        return DB.getFirstColumn("SELECT " + column.column() + " FROM regions WHERE `region_name` = ?", column.column(), regionId);
    }

    public List<String> getColumnAsList(Column column) {
        String serializedList = getColumnAsString(column);
        return ListSerializer.deserialize(serializedList);
    }

    @SneakyThrows
    private void updateColumn(Column column, String value) {
        DB.executeUpdateAsync("UPDATE regions SET " + column.column() + " = ? WHERE region_name = ?", value, regionId);
    }

    public List<String> owners() {
        return getColumnAsList(Column.OWNERS);
    }

    public boolean isOwner(String owner) {
        return owners().contains(owner);
    }

    public void addOwner(String name) {
        changeList(owners(), Column.OWNERS, name, true);
    }

    public void removeOwner(String name) {
        changeList(owners(), Column.OWNERS, name, false);
    }

    public List<String> members() {
        return getColumnAsList(Column.MEMBERS);
    }

    public boolean isMember(String member) {
        return members().contains(member);
    }

    public void addMember(String name) {
        changeList(members(), Column.MEMBERS, name, true);
    }

    public void removeMember(String name) {
        changeList(members(), Column.MEMBERS, name, false);
    }

    public String date() {
        return getColumnAsString(Column.DATE);
    }

    public Location location() {
        String serializedLocation = getColumnAsString(Column.LOCATION);
        return LocationSerializer.deserialize(serializedLocation);
    }

    public void location(Location location) {
        updateColumn(Column.LOCATION, LocationSerializer.serialize(location));
    }

    public List<String> effects() {
        return getColumnAsList(Column.EFFECTS);
    }

    public void effects(String effects) {
        updateColumn(Column.EFFECTS, effects);
    }

    public boolean hasActiveEffects() {
        return !effects().isEmpty();
    }

    public List<String> activeEffects() {
        return getColumnAsList(Column.ACTIVE_EFFECTS);
    }

    public int maxOwners() {
        return getColumnAsInt(Column.MAX_OWNERS);
    }

    public void maxOwners(String value) {
        updateColumn(Column.MAX_OWNERS, value);
    }

    public void addMaxOwners(int amount) {
        int count = maxOwners() + amount;
        updateColumn(Column.MAX_OWNERS, Integer.toString(count));
    }

    public int maxMembers() {
        return getColumnAsInt(Column.MAX_MEMBERS);
    }

    public void maxMembers(String value) {
        updateColumn(Column.MAX_MEMBERS, value);
    }

    public void addMaxMembers(int amount) {
        int count = maxMembers() + amount;
        updateColumn(Column.MAX_MEMBERS, Integer.toString(count));
    }

    private void changeList(List<String> list, Column column, String name, boolean add) {
        List<String> newList;

        if (list == null) {
            newList = new ArrayList<>();
        } else {
            newList = new ArrayList<>(list);
        }

        if (add) {
            newList.add(name);
        } else {
            newList.remove(name);
        }

        String serializedList = ListSerializer.serialize(newList);
        updateColumn(column, serializedList);
    }
}
