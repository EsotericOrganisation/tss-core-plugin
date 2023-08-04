package net.slqmy.tss_core.data.type;

import java.util.UUID;

public class PlayerData {
	protected UUID uuid;
	protected PlayerPreferences playerPreferences;

	public UUID getUuid() {
		return uuid;
	}

	public PlayerPreferences getPlayerPreferences() {
		return playerPreferences;
	}
}
