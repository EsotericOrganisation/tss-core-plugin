package net.slqmy.tss_core.data.type;

import java.util.UUID;

public class PlayerData {
	protected UUID uuid;
	protected Preferences preferences;

	public UUID getUuid() {
		return uuid;
	}

	public Preferences getPreferences() {
		return preferences;
	}
}
