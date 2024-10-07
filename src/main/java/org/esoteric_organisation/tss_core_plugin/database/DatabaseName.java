package org.esoteric_organisation.tss_core_plugin.database;

public enum DatabaseName {
    PLAYERS;

    private final String name;

    DatabaseName() {
        name = name().toLowerCase();
    }

    public String getName() {
        return name;
    }
}
