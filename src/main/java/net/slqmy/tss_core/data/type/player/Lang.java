package net.slqmy.tss_core.data.type.player;

public enum Lang {
	ENGLISH_UK("en-uk");

	public static final Lang DEFAULT_LANG = Lang.ENGLISH_UK;

	private final String code;

	Lang(String langCode) {
		code = langCode;
	}

	public String getCode() {
		return code;
	}
}
