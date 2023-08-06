package net.slqmy.tss_core.database.collection_name;

import net.slqmy.tss_core.database.DatabaseName;

public enum PlayersCollectionName implements CollectionName {
	PLAYER_PROFILES;

	private final static DatabaseName database = DatabaseName.PLAYERS;
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
