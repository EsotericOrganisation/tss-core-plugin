package net.slqmy.tss_core.data.type;

public enum Lang {
	ENGLISH_UK("en-uk");

	private final String langCode;

	Lang(String langCode) {
		this.langCode = langCode;
	}

	public String getLangCode() {
		return langCode;
	}
}
