package net.slqmy.tss_core.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.slqmy.tss_core.type.Colour;

public class MessageUtil {
	public static TextComponent format(Object value) {
		return value == null
						? Component.text("null", Colour.PINK.asTextColour())
						: value instanceof Boolean ? (boolean) value ?
						Component.text("true", Colour.SLIME.asTextColour())
						: Component.text("false", Colour.RED.asTextColour())
						: value instanceof Integer || value instanceof Float || value instanceof Double
						? Component.text(value.toString(), Colour.SKY_BLUE.asTextColour())
						: value instanceof String
						? LegacyComponentSerializer.legacyAmpersand().deserialize((String) value)
						: Component.text(value.toString());
	}
}
