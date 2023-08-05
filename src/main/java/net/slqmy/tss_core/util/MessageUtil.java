package net.slqmy.tss_core.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.slqmy.tss_core.type.Colour;
import org.jetbrains.annotations.NotNull;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MessageUtil {

	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");

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

	public static @NotNull TextComponent getPipeTextComponent() {
		return Component.text("| ", Colour.GREY.asTextColour());
	}

	public static @NotNull TextComponent getColonTextComponent() {
		return Component.text(": ", Colour.LIGHT_GREY.asTextColour());
	}

	public static @NotNull String formatNumber(double number) {
		DECIMAL_FORMAT.setRoundingMode(RoundingMode.HALF_UP);
		return DECIMAL_FORMAT.format(number);
	}
}
