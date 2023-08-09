package net.slqmy.tss_core.type;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;

public class Colour implements TextColor {

	public static Colour RED = new Colour(0xff0000);
	public static Colour BLOOD_RED = new Colour(0xb3312c);
	public static Colour MAROON = new Colour(0xb3312c);
	public static Colour ORANGE = new Colour(0xf7a52a);
	public static Colour LIGHT_ORANGE = new Colour(0xf59444);
	public static Colour LIGHTER_ORANGE = new Colour(0xffa844);
	public static Colour BANANA = new Colour(0xffcb2f);
	public static Colour YELLOW = new Colour(0xffeb00);
	public static Colour SLIME = new Colour(0x51f54e);
	public static Colour LIME = new Colour(0x41cd34);
	public static Colour MINT = new Colour(0x00fc89);
	public static Colour GREEN = new Colour(0x46a41a);
	public static Colour DARK_GREEN = new Colour(0x3b891a);
	public static Colour DARKER_GREEN = new Colour(0x3b511a);
	public static Colour SKY_BLUE = new Colour(0x5ea8f2);
	public static Colour LIGHT_BLUE = new Colour(0x6689d3);
	public static Colour OCEAN_BLUE = new Colour(0x0073ff);
	public static Colour CYAN = new Colour(0x287697);
	public static Colour DARK_BLUE = new Colour(0x253192);
	public static Colour BLURPLE = new Colour(0x5865F2);
	public static Colour PURPLE = new Colour(0x7b2fbe);
	public static Colour MAGENTA = new Colour(0xfb114c);
	public static Colour LIGHT_PURPLE = new Colour(0xc354cd);
	public static Colour PINK = new Colour(0xca6cd8);
	public static Colour GREY = new Colour(0x404040);
	public static Colour LIGHT_GREY = new Colour(0x909090);
	public static Colour WHITE = new Colour(0xffffff);

	private final int hexValue;

	Colour(int hexValue) {
		this.hexValue = hexValue;
	}

	@Override
	public int value() {
		return hexValue;
	}

	public Color asBukkitColour() {
		return Color.fromRGB(hexValue);
	}
}
