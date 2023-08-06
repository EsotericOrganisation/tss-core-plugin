package net.slqmy.tss_core.data;

public enum Message {
	// - The Slimy Swamp Messages

	// General

	THE_SLIMY_SWAMP,

	// TSS-Core
	UNABLE_TO_LOAD_DATA,

	// TSS-SlimeLabs
	ACTIVE_LAUNCH_ANALYSIS_TASK,
	LAUNCH,
	VIEW_LAUNCH_STATISTICS,
	STOP_LAUNCH_ANALYSIS,
	ENTITY,
	TIME,
	ON_GROUND,

	NO_ANALYSIS_TASK,
	LAUNCH_STATISTICS,

	// TSS-Lobby

	WELCOME_SUBTITLE,
	WELCOME_CHAT_MESSAGE,

	// - Errors and In-Plugin Messages

	// TSS-Core
	SUCCESSFUL_DATABASE_CONNECTION,
	DATABASE_CONNECTION_FAILED,

	ERROR_LOADING_PLAYER_PROFILE;

	private final String key;

	Message() {
		key = name().toLowerCase().replace('_', '-');
	}

	public String getKey() {
		return key;
	}
}
