package ru.mcsnapix.snapistones.plugin.api.region;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.api.ProtectedBlock;
import ru.mcsnapix.snapistones.plugin.database.Column;
import ru.mcsnapix.snapistones.plugin.database.Database;
import ru.mcsnapix.snapistones.plugin.serializers.ListSerializer;
import ru.mcsnapix.snapistones.plugin.serializers.LocationSerializer;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true)
public class Region {
    private final static SnapiStones plugin = SnapiStones.get();
    @NonNull
    private final String id;
    @NonNull
    private final ProtectedRegion protectedRegion;
    private final Database database;

    private final List<String> owners = new ArrayList<>();
    private final List<String> members = new ArrayList<>();

    @Getter private final int date;
    @NonNull @Getter
    private final ProtectedBlock protectedBlock;

    @Nullable
    private Location homeLocation;
    @Nullable
    private List<String> effects;
    @Nullable
    private List<String> activeEffects;
    private int maxOwners;
    private int maxMembers;

    Region(@NonNull String id, @NonNull ProtectedRegion protectedRegion) {
        this.id = id;
        this.protectedRegion = protectedRegion;
        this.database = new Database(id);
        date = database.getColumnAsInt(Column.DATE);
        protectedBlock = new ProtectedBlock(this);
    }

    public String name() {
        return id;
    }

    public List<String> owners(boolean update) {
        if (owners.isEmpty() || update) {
            owners.addAll(database.getColumnAsList(Column.OWNERS));
        }
        return owners;
    }

    public List<String> members(boolean update) {
        if (members.isEmpty() || update) {
            this.owners.addAll(database.getColumnAsList(Column.MEMBERS));
        }
        return members;
    }

    public Location homeLocation(boolean update) {
        if (homeLocation == null || update) {
            String location = database.getColumnAsString(Column.HOME_LOCATION);
            homeLocation = LocationSerializer.deserialize(location);
        }
        return homeLocation;
    }

    public boolean hasHomeLocation() {
        return homeLocation() != null;
    }

    public void homeLocation(Location location) {
        database.updateColumn(Column.HOME_LOCATION, LocationSerializer.serialize(location));
        homeLocation(true);
    }

    public List<String> effects(boolean update) {
        if (effects == null || update) {
            effects = database.getColumnAsList(Column.EFFECTS);
        }
        return effects;
    }

    public boolean hasEffect(String effect) {
        return effects().contains(effect);
    }

    public void setEffects(String effects) {
        database.updateColumn(Column.EFFECTS, effects);
        effects(true);
    }

    public List<String> activeEffects(boolean update) {
        if (activeEffects == null || update) {
            activeEffects = database.getColumnAsList(Column.ACTIVE_EFFECTS);
        }
        return activeEffects;
    }

    public boolean hasActiveEffects() {
        return !activeEffects().isEmpty();
    }

    public boolean hasActiveEffect(String effect) {
        return activeEffects().contains(effect);
    }

    public void activeEffect(String effects) {
        database.updateColumn(Column.ACTIVE_EFFECTS, effects);
        activeEffects(true);
    }

    public void addOwners(String name) {
        List<String> owners = owners(false);
        owners.add(name);
        protectedRegion.getOwners().addPlayer(name);
        database.updateColumn(Column.OWNERS, ListSerializer.serialize(owners));
        owners(true);
    }

    public void addMembers(String name) {
        List<String> members = members();
        members.add(name);
        protectedRegion.getMembers().addPlayer(name);
        database.updateColumn(Column.MEMBERS, ListSerializer.serialize(members));
        owners(true);
    }

    public int maxOwners() {
        if (maxOwners == 0) {
            maxOwners = database.getColumnAsInt(Column.MAX_OWNERS);
        }
        return maxOwners;
    }

    public void maxOwners(int maxOwners) {
        database.updateColumn(Column.MAX_OWNERS, maxOwners);
    }

    public int maxMembers() {
        return database.getColumnAsInt(Column.MAX_MEMBERS);
    }

    public void maxMembers(int maxMembers) {
        database.updateColumn(Column.MAX_MEMBERS, maxMembers);
    }

    public List<String> owners() {
        return owners(false);
    }

    public List<String> members() {
        return owners(false);
    }
}
