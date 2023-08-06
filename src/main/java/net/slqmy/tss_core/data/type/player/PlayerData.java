package net.slqmy.tss_core.data.type.player;

import java.util.UUID;

public class PlayerData {
	protected UUID uuid;

	protected PlayerPreferences playerPreferences;
	protected PlayerStats playerStats;

	public UUID getUuid() {
		return uuid;
	}

	public PlayerPreferences getPlayerPreferences() {
		return playerPreferences;
	}

	public PlayerStats getPlayerStats() {
		return playerStats;
	}
}
