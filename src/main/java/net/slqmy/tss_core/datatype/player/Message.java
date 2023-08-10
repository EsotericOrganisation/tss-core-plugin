package net.slqmy.tss_core.datatype.player;

public enum Message {
	// - The Slimy Swamp Messages

	// General
	THE_SLIMY_SWAMP,
	NONEXISTENT_PLAYER,

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

	// TSS-Ranks
	NONEXISTENT_RANK,
	RANK_ALREADY_SET,
	RANK_ALREADY_SET_OTHER,
	RANK_SUCCESSFULLY_SET,
	RANK_SUCCESSFULLY_SET_OTHER,
	RANK_SET_NOTIFICATION;

	private final String key;

	Message() {
		key = name().toLowerCase().replace('_', '-');
	}

	public String getKey() {
		return key;
	}
}
