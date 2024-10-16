package org.esoteric.tss.minecraft.plugins.core.database.collections;

import org.esoteric.tss.minecraft.plugins.core.database.DatabaseName;

public enum PlayersCollectionName implements CollectionName {

    PLAYER_PROFILES;

    private static final DatabaseName database = DatabaseName.PLAYERS;
    private final String name;

    PlayersCollectionName() {
        name = name().toLowerCase();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public DatabaseName getDatabaseName() {
        return database;
    }
}
