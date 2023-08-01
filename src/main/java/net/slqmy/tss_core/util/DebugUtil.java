package net.slqmy.tss_core.util;

import org.jetbrains.annotations.NotNull;

public class DebugUtil {
	private static final String DEBUG_PREFIX = "[Debug]";

	public static void log(Object @NotNull ... values) {
		for (Object value : values) {
			LogUtil.log(DEBUG_PREFIX + " " + value);
		}
	}

	public static void handleException(@NotNull Exception exception, String message) {
		LogUtil.error(message);
		LogUtil.error(exception.getMessage());
		LogUtil.error(exception);

		exception.printStackTrace();
	}
}
