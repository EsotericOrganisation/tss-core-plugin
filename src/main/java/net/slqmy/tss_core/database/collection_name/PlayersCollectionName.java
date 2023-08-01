package net.slqmy.tss_core.database.collection_name;

public enum PlayersCollectionName {
	PLAYER_PROFILES;

	private final String name;

	PlayersCollectionName() {
		name = name().toLowerCase();
	}

	public String getName() {
		return name;
	}
}
