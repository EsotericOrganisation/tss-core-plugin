package net.slqmy.tss_core.data;

public enum Message {
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
	LAUNCH_STATISTICS;

	private final String key;

	Message() {
		key = name().toLowerCase().replace('_', '-');
	}

	public String getKey() {
		return key;
	}
}
