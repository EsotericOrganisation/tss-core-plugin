package net.slqmy.tss_core.type;

import net.kyori.adventure.text.format.TextColor;

public enum Colour {

	RED("ff0000"),
	ORANGE("f7a52a"),
	YELLOW("cfcf55"),
	SLIME("51f54e"),
	SKY_BLUE("5ea8f2"),
	BLURPLE("5865F2"),
	PINK("ca6cd8"),
	GREY("404040"),
	LIGHT_GREY("909090");

	private final String hexString;

	Colour(String hexString) {
		this.hexString = hexString;
	}

	public String getHexString() {
		return hexString;
	}

	public TextColor asTextColour() {
		return TextColor.fromHexString("#" + hexString);
	}
}
