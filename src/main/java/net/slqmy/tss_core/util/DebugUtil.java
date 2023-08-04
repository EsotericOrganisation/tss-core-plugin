package net.slqmy.tss_core.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

public class DebugUtil {

	private static final Component DEBUG_PREFIX = LegacyComponentSerializer.legacyAmpersand().deserialize("[&eDebug&r]");
	private final static TextComponent ERROR_PREFIX = LegacyComponentSerializer.legacyAmpersand().deserialize("[&cError&r]");

	public static void log(Object @NotNull ... values) {
		for (Object value : values) {
			LogUtil.log(
							DEBUG_PREFIX.append(Component.text(" ")).append(MessageUtil.format(value))
			);
		}
	}

	public static void error(@NotNull Object error) {
		LogUtil.log(
						ERROR_PREFIX
										.append(Component.text(" "))
										.append(MessageUtil.format("&c" + error))
		);
	}

	public static void handleException(String message, @NotNull Exception exception) {
		if (message != null) {
			error(LegacyComponentSerializer.legacyAmpersand().deserialize("&c" + message));
		}

		error(exception.getMessage());
		error(exception);

		exception.printStackTrace();
	}

	public static void handleException(@NotNull Exception exception) {
		handleException(null, exception);
	}
}
