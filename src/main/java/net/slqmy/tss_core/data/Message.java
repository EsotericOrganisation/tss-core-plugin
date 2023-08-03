package net.slqmy.tss_core.data;

public enum Message {
	UNABLE_TO_LOAD_DATA,
	CLICK_TO_JOIN;

	private final String key;

	Message() {
		key = name().toLowerCase().replace('_', '-');
	}

	public String getKey() {
		return key;
	}
}
