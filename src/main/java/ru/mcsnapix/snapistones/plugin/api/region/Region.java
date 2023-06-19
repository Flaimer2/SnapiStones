package ru.mcsnapix.snapistones.plugin.api.region;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ru.mcsnapix.snapistones.plugin.Placeholders;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.api.ProtectedBlock;
import ru.mcsnapix.snapistones.plugin.api.SnapApi;
import ru.mcsnapix.snapistones.plugin.database.Column;
import ru.mcsnapix.snapistones.plugin.database.Database;
import ru.mcsnapix.snapistones.plugin.modules.home.config.HomeConfig;
import ru.mcsnapix.snapistones.plugin.serializers.ListSerializer;
import ru.mcsnapix.snapistones.plugin.serializers.LocationSerializer;
import ru.mcsnapix.snapistones.plugin.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a region with various properties and methods for managing
 * owners, members, effects and other data associated with the region.
 */
@Accessors(fluent = true)
public class Region {
    @NonNull
    private final String id;
    @NonNull
    private final ProtectedRegion protectedRegion;
    @Getter
    private final Database database;
    @Getter
    private final int date;
    @Getter
    private final String author;
    @NonNull
    @Getter
    private final ProtectedBlock protectedBlock;
    private List<String> owners = new ArrayList<>();
    private List<String> members = new ArrayList<>();
    @Nullable
    private Location homeLocation;
    @Nullable
    private List<String> effects;
    @Nullable
    private List<String> activeEffects;
    private int maxOwners;
    private int maxMembers;

    /**
     * Constructs a new Region instance with the given ID and ProtectedRegion.
     *
     * @param id              the region ID
     * @param protectedRegion the WorldGuard ProtectedRegion
     */
    Region(@NonNull String id, @NonNull ProtectedRegion protectedRegion) {
        this.id = id;
        this.protectedRegion = protectedRegion;
        this.database = new Database(id);
        date = database.getColumnAsInt(Column.DATE);
        author = database.getColumnAsString(Column.AUTHOR);
        protectedBlock = new ProtectedBlock(this);
    }

    /**
     * Returns the region ID.
     *
     * @return the region ID
     */
    public String name() {
        return id;
    }

    /**
     * Returns the list of region owners.
     *
     * @return the list of region owners
     */
    public List<String> owners() {
        if (owners.isEmpty()) {
            updateOwners();
        }
        return owners;
    }

    /**
     * Updates the list of region owners from the database.
     */
    public void updateOwners() {
        owners = database.getColumnAsList(Column.OWNERS);
    }

    /**
     * Checks if the given player is an owner of the region.
     *
     * @param name the player's name
     * @return true if the player is an owner, false otherwise
     */
    public boolean hasOwnerInRegion(String name) {
        return Util.ignoreCaseContains(owners(), name);
    }

    /**
     * Returns the list of region members.
     *
     * @return the list of region members
     */
    public List<String> members() {
        if (members.isEmpty()) {
            updateMembers();
        }
        return members;
    }

    /**
     * Updates the list of region members from the database.
     */
    public void updateMembers() {
        members = database.getColumnAsList(Column.MEMBERS);
    }

    /**
     * Checks if the given player is a member of the region.
     *
     * @param name the player's name
     * @return true if the player is a member, false otherwise
     */
    public boolean hasMemberInRegion(String name) {
        return Util.ignoreCaseContains(members(), name);
    }

    /**
     * Checks if there is a player in the region.
     *
     * @param name the player's name
     * @return true if the player is in the region, false otherwise
     */
    public boolean hasPlayerInRegion(String name) {
        return hasMemberInRegion(name) || hasOwnerInRegion(name);
    }

    /**
     * Returns the home location of the region.
     *
     * @return the home location of the region
     */
    public Location homeLocation() {
        return homeLocation;
    }

    /**
     * Updates the home location of the region from the database.
     */
    public void updateHomeLocation() {
        homeLocation = LocationSerializer.deserialise(database.getColumnAsString(Column.HOME_LOCATION));
    }

    /**
     * Checks if the region has a home location.
     *
     * @return true if the region has a home location, false otherwise
     */
    public boolean hasHomeLocation() {
        return homeLocation() != null;
    }

    /**
     * Sets the home location of the region and updates the database.
     *
     * @param location the new home location
     */
    public void homeLocation(Location location) {
        database.updateColumn(Column.HOME_LOCATION, LocationSerializer.serialise(location));
        updateHomeLocation();
    }

    /**
     * Teleports the player to the region's home location
     *
     * @param player the player to be teleported
     */
    public void teleportHomeLocation(Player player) {
        Placeholders placeholders = new Placeholders(player, this);
        HomeConfig homeConfig = SnapiStones.get().getModules().home().homeConfig().data();

        if (!hasHomeLocation()) {
            placeholders.sendMessage(homeConfig.commands().home().noHomeInRegion());
            return;
        }

        player.teleport(homeLocation());
        placeholders.sendMessage(homeConfig.commands().home().success());
    }

    /**
     * Returns the list of effects for the region.
     *
     * @return the list of effects for the region
     */
    public List<String> effects() {
        if (effects == null) {
            updateEffects();
        }
        return effects;
    }

    /**
     * Updates the list of effects for the region from the database.
     */
    public void updateEffects() {
        effects = database.getColumnAsList(Column.EFFECTS);
    }

    /**
     * Checks if the region has a specific effect.
     *
     * @param effect the effect to check for
     * @return true if the region has the effect, false otherwise
     */
    public boolean hasEffect(String effect) {
        return Util.ignoreCaseContains(effects(), effect);
    }

    /**
     * Sets the effects for the region and updates the database.
     *
     * @param effects the new effects
     */
    public void setEffects(String effects) {
        database.updateColumn(Column.EFFECTS, effects);
        updateEffects();
    }

    public void addEffect(String effect) {
        List<String> effects = new ArrayList<>(effects());
        effects.add(effect);
        database.updateColumn(Column.EFFECTS, ListSerializer.serialise(effects));
        updateEffects();
    }

    /**
     * Returns the list of active effects for the region.
     *
     * @return the list of active effects for the region
     */
    public List<String> activeEffects() {
        if (activeEffects == null) {
            updateActiveEffects();
        }
        return activeEffects;
    }

    /**
     * Updates the list of active effects for the region from the database.
     */
    public void updateActiveEffects() {
        activeEffects = database.getColumnAsList(Column.ACTIVE_EFFECTS);
    }

    /**
     * Checks if the region has any active effects.
     *
     * @return true if the region has active effects, false otherwise
     */
    public boolean hasActiveEffects() {
        return !activeEffects().isEmpty();
    }

    /**
     * Checks if the region has a specific active effect.
     *
     * @param effect the active effect to check for
     * @return true if the region has the active effect, false otherwise
     */
    public boolean hasActiveEffect(String effect) {
        return Util.ignoreCaseContains(activeEffects(), effect);
    }

    /**
     * Sets the active effects for the region and updates the database.
     *
     * @param effects the new active effects
     */
    public void activeEffect(String effects) {
        database.updateColumn(Column.ACTIVE_EFFECTS, effects);
        updateActiveEffects();
    }

    public void addActiveEffect(String effect) {
        List<String> effects = new ArrayList<>(activeEffects());
        effects.add(effect);
        database.updateColumn(Column.ACTIVE_EFFECTS, ListSerializer.serialise(effects));
        updateActiveEffects();
    }

    public void removeActiveEffect(String effect) {
        List<String> effects = new ArrayList<>(activeEffects());
        effects.remove(effect);
        database.updateColumn(Column.ACTIVE_EFFECTS, ListSerializer.serialise(effects));
        updateActiveEffects();
    }

    /**
     * Adds a player as an owner of the region and updates the database.
     *
     * @param name the player's name
     */
    public void addOwner(String name) {
        List<String> owners = new ArrayList<>(owners());
        owners.add(name);
        protectedRegion.getOwners().addPlayer(name);
        database.updateColumn(Column.OWNERS, ListSerializer.serialise(owners));
        updateOwners();
    }

    /**
     * Removes the player as an owner of the region and updates the database.
     *
     * @param name the player's name
     */
    public void removeOwner(String name) {
        protectedRegion.getOwners().removePlayer(name);
        List<String> owners = new ArrayList<>(owners());
        owners.remove(name);
        database.updateColumn(Column.OWNERS, ListSerializer.serialise(owners));
        updateOwners();
    }

    /**
     * Adds a player as a member of the region and updates the database.
     *
     * @param name the player's name
     */
    public void addMember(String name) {
        List<String> members = new ArrayList<>(members());
        members.add(name);
        protectedRegion.getMembers().addPlayer(name);
        database.updateColumn(Column.MEMBERS, ListSerializer.serialise(members));
        updateMembers();
    }

    /**
     * Removes the player as a member of the region and updates the database.
     *
     * @param name the player's name
     */
    public void removeMember(String name) {
        protectedRegion.getMembers().removePlayer(name);
        List<String> members = new ArrayList<>(members());
        members.remove(name);
        database.updateColumn(Column.MEMBERS, ListSerializer.serialise(members));
        updateMembers();
    }

    /**
     * Returns the maximum number of owners for the region.
     *
     * @return the maximum number of owners for the region
     */
    public int maxOwners() {
        if (maxOwners == 0) {
            updateMaxOwners();
        }
        return maxOwners;
    }

    /**
     * Updates the maximum number of owners for the region from the database.
     */
    public void updateMaxOwners() {
        maxOwners = database.getColumnAsInt(Column.MAX_OWNERS);
    }

    /**
     * Sets the maximum number of owners for the region and updates the database.
     *
     * @param maxOwners the new maximum number of owners
     */
    public void maxOwners(int maxOwners) {
        database.updateColumn(Column.MAX_OWNERS, maxOwners);
        updateMaxOwners();
    }

    /**
     * Returns the maximum number of members for the region.
     *
     * @return the maximum number of members for the region
     */
    public int maxMembers() {
        if (maxMembers == 0) {
            updateMaxMembers();
        }
        return maxMembers;
    }

    /**
     * Updates the maximum number of members for the region from the database.
     */
    public void updateMaxMembers() {
        maxMembers = database.getColumnAsInt(Column.MAX_MEMBERS);
    }

    /**
     * Sets the maximum number of members for the region and updates the database.
     *
     * @param maxMembers the new maximum number of members
     */
    public void maxMembers(int maxMembers) {
        database.updateColumn(Column.MAX_MEMBERS, maxMembers);
        updateMaxMembers();
    }

    public List<Player> playersInRegion() {
        return Bukkit.getOnlinePlayers().stream().filter(player -> SnapApi.getRegion(player.getLocation()) == this && hasPlayerInRegion(player.getName())).collect(Collectors.toList());
    }
}

