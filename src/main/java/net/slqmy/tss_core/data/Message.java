package net.slqmy.tss_core.data;

public enum Message {
	UNABLE_TO_LOAD_DATA;

	private String key;

	Message() {
		key = name().toLowerCase().replace('_', '-');
	}

	public String getKey() {
		return key;
	}
}
