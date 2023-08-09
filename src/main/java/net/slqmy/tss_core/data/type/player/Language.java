package net.slqmy.tss_core.data.type.player;

public enum Language {
	ENGLISH_UK("en-gb");

	public static final Language DEFAULT_LANGUAGE = ENGLISH_UK;

	private String code;

	Language(String langCode) {
		code = langCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String languageCode) {
		this.code = languageCode;
	}
}
