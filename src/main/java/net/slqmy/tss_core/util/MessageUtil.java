package net.slqmy.tss_core.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.slqmy.tss_core.type.Colour;

public class MessageUtil {
	public static TextComponent format(Object value) {
		return value == null
						? Component.text("null", Colour.PINK.asTextColour())
						: value instanceof String
						? LegacyComponentSerializer.legacySection().deserialize((String) value)
						: Component.text(value.toString());
	}
}
