package ru.mcsnapix.snapistones.plugin.database;

import co.aikar.idb.DB;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.bukkit.Location;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.serializers.ListSerializer;
import ru.mcsnapix.snapistones.plugin.serializers.LocationSerializer;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private final SnapiStones plugin = SnapiStones.get();
    @NonNull
    private final ProtectedRegion region;
    private final String regionId;

    public Database(@NonNull ProtectedRegion region) {
        this.region = region;
        regionId = region.getId();
    }

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
        return DB.getFirstColumn("SELECT " + column.getName() + " FROM regions WHERE `region_name` = ?", regionId);
    }

    @SneakyThrows
    private String getColumnAsString(Column column) {
        return DB.getFirstColumn("SELECT " + column.getName() + " FROM regions WHERE `region_name` = ?", regionId);
    }

    public List<String> getColumnAsList(Column column) {
        String serializedList = getColumnAsString(column);
        if (serializedList == null) return null;
        return ListSerializer.deserialize(serializedList);
    }

    @SneakyThrows
    private void updateColumn(Column column, String value) {
        DB.executeUpdateAsync("UPDATE regions SET " + column.getName() + " = ? WHERE region_name = ?", value, regionId);
    }

    @SneakyThrows
    private void updateColumn(Column column, int value) {
        DB.executeUpdateAsync("UPDATE regions SET " + column.getName() + " = ? WHERE region_name = ?", value, regionId);
    }

    public List<String> owners() {
        return getColumnAsList(Column.OWNERS);
    }

    public boolean isOwner(String owner) {
        List<String> owners = owners();

        if (owners == null) return false;
        return owners.contains(owner);
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
        List<String> members = members();

        if (members == null) return false;
        return members.contains(member);
    }

    public void addMember(String name) {
        changeList(members(), Column.MEMBERS, name, true);
    }

    public void removeMember(String name) {
        changeList(members(), Column.MEMBERS, name, false);
    }

    public String date() {
        return Integer.toString(getColumnAsInt(Column.DATE));
    }

    public Location location() {
        String serializedLocation = getColumnAsString(Column.LOCATION);
        if (serializedLocation == null) return null;
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

    public void maxOwners(int value) {
        updateColumn(Column.MAX_OWNERS, value);
    }

    public void addMaxOwners(int amount) {
        int count = maxOwners() + amount;
        updateColumn(Column.MAX_OWNERS, count);
    }

    public int maxMembers() {
        return getColumnAsInt(Column.MAX_MEMBERS);
    }

    public void maxMembers(int value) {
        updateColumn(Column.MAX_MEMBERS, value);
    }

    public void addMaxMembers(int amount) {
        int count = maxMembers() + amount;
        updateColumn(Column.MAX_MEMBERS, count);
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
