package net.slqmy.tss_core.data.type.player;

public class PlayerPreferences {

	private Language language;

	public PlayerPreferences() {
		language = Language.DEFAULT_LANGUAGE;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}
}
