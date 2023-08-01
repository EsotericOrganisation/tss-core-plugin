package net.slqmy.tss_core.util;

import org.jetbrains.annotations.NotNull;

public class DebugUtil {
	public static void handleException(@NotNull Exception exception, String message) {
		LogUtil.error(message);
		LogUtil.error(exception.getMessage());
		LogUtil.error(exception);

		exception.printStackTrace();
	}
}
