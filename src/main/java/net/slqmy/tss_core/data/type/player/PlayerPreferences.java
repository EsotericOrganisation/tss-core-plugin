package net.slqmy.tss_core.data.type.player;

public class PlayerPreferences {

	private final Lang lang;

	public PlayerPreferences() {
		lang = Lang.DEFAULT_LANG;
	}

	public Lang getLang() {
		return lang;
	}
}
