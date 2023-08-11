package net.slqmy.tss_core.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.slqmy.tss_core.datatype.Colour;
import org.jetbrains.annotations.NotNull;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageUtil {

  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");

  private static final int DEFAULT_HEADING_DASH_COUNT = 4;

  public static @NotNull TextComponent getPipe() {
	return Component.text("| ", Colour.GREY);
  }

  public static @NotNull TextComponent getDash(int repeatCount) {
	return Component.text("-".repeat(repeatCount), Colour.SKY_BLUE);
  }

  public static @NotNull TextComponent getDash() {
	return getDash(1);
  }

  public static @NotNull TextComponent getColon() {
	return Component.text(": ", Colour.LIGHT_GREY);
  }

  public static @NotNull TextComponent getLeftSquareBracket() {
	return Component.text("[", Colour.LIGHT_GREY);
  }

  public static @NotNull TextComponent getRightSquareBracket() {
	return Component.text("]", Colour.LIGHT_GREY);
  }

  public static @NotNull TextComponent getComma() {
	return Component.text(", ", Colour.GREY);
  }

  public static TextComponent format(Object value) {
	return value == null
			? Component.text("null", Colour.PINK)
			: value instanceof Boolean ?
			Component.text((boolean) value, (boolean) value ? Colour.SLIME : Colour.RED)
			: value instanceof Number
			? Component.text(value.toString(), Colour.SKY_BLUE)
			: value instanceof String
			? LegacyComponentSerializer.legacyAmpersand().deserialize((String) value)
			: value instanceof ArrayList<?>
			? createCompactArray((ArrayList<?>) value)
			: value instanceof Object[]
			? createCompactArray((Object[]) value)
			: value instanceof TextComponent
			? (TextComponent) value
			: Component.text(value.toString());
  }

  public static @NotNull String formatNumber(double number) {
	DECIMAL_FORMAT.setRoundingMode(RoundingMode.HALF_UP);
	return DECIMAL_FORMAT.format(number);
  }

  public static @NotNull TextComponent createHeading(TextComponent headingTitle, int dashCount) {
	return getDash(dashCount)
			.append(Component.space())
			.append(headingTitle)
			.append(Component.space())
			.append(getDash(dashCount));
  }

  public static @NotNull TextComponent createHeading(TextComponent headingTitle) {
	return createHeading(headingTitle, DEFAULT_HEADING_DASH_COUNT);
  }

  public static @NotNull TextComponent createHeading(String headingTitle, int dashCount) {
	return createHeading(format(headingTitle), dashCount);
  }

  public static @NotNull TextComponent createHeading(String headingTitle) {
	return createHeading(format(headingTitle), DEFAULT_HEADING_DASH_COUNT);
  }

  public static @NotNull TextComponent createCompactArray(@NotNull List<?> list) {
	TextComponent component = getLeftSquareBracket();

	Object previousElement = null;

	for (int i = 0; i < list.size(); i++) {
	  Object element = list.get(i);
	  component = component.append(format(element)).append(getComma()).append(Component.space());

	  previousElement = element;
	}

	return component;
  }

  public static @NotNull TextComponent createCompactArray(Object @NotNull [] array) {
	return createCompactArray(Arrays.asList(array));
  }
}
