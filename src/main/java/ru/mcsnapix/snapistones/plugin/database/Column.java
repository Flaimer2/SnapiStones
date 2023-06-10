package ru.mcsnapix.snapistones.plugin.database;

public enum Column {
    OWNERS("owners_name"),
    MEMBERS("members_name"),
    DATE("creation_date"),
    HOME_LOCATION("home_location"),
    LOCATION("block_location"),
    MATERIAL("block_material"),
    EFFECTS("effects"),
    ACTIVE_EFFECTS("active_effects"),
    MAX_OWNERS("max_owners"),
    MAX_MEMBERS("max_members");

    private final String name;

    Column(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
