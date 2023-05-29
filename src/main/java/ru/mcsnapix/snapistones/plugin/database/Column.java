package ru.mcsnapix.snapistones.plugin.database;

public enum Column {
    OWNERS("owners_name"),
    MEMBERS("members_name"),
    DATE("creation_date"),
    LOCATION("location"),
    EFFECTS("effects"),
    ACTIVE_EFFECTS("active_effects"),
    MAX_OWNERS("max_owners"),
    MAX_MEMBERS("max_members");

    private final String column;

    Column(String column) {
        this.column = column;
    }

    public String column() {
        return column;
    }
}
