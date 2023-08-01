package net.slqmy.tss_core.util;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public class LogUtil {
	private static final Logger LOGGER = Bukkit.getLogger();

	public static void log(String message) {
		LOGGER.info(message);
	}

	public static void error(@NotNull Object message) {
		String finalMessage = message.toString();

		LOGGER.severe(finalMessage);
	}
}
