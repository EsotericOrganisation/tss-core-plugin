package net.slqmy.tss_core.data;

public enum Message {
	UNABLE_TO_LOAD_DATA,
	NOT_PLAYER_COMMAND,
	NOT_CONSOLE_COMMAND,
	NOT_COMMAND_BLOCK_COMMAND,
	INVALID_COMMAND_USAGE,
	CLICK_TO_JOIN;

	private final String key;

	Message() {
		key = name().toLowerCase().replace('_', '-');
	}

	public String getKey() {
		return key;
	}
}
