package net.slqmy.tss_core.type;

import net.kyori.adventure.text.format.TextColor;

public class Colour implements TextColor {

	public static Colour RED = new Colour(0xff0000);
	public static Colour ORANGE = new Colour(0xf7a52a);
	public static Colour YELLOW = new Colour(0xcfcf55);
	public static Colour SLIME = new Colour(0x51f54e);
	public static Colour SKY_BLUE = new Colour(0x5ea8f2);
	public static Colour BLURPLE = new Colour(0x5865F2);
	public static Colour PINK = new Colour(0xca6cd8);
	public static Colour GREY = new Colour(0x404040);
	public static Colour LIGHT_GREY = new Colour(0x909090);

	private final int hexValue;

	Colour(int hexValue) {
		this.hexValue = hexValue;
	}

	@Override
	public int value() {
		return hexValue;
	}
}
