package net.slqmy.tss_core.type;

import net.kyori.adventure.text.format.TextColor;

public enum Colour {

	BLURPLE("5865F2"),
	PINK("ca6cd8");

	private final String hexString;

	Colour(String hexString) {
		this.hexString = "#" + hexString;
	}

	public String getHexString() {
		return hexString;
	}

	public TextColor asTextColour() {
		return TextColor.fromHexString("#" + hexString);
	}
}
