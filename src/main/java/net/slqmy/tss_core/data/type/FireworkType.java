package net.slqmy.tss_core.data.type;

import net.slqmy.tss_core.type.Colour;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;

import java.util.Arrays;

public enum FireworkType {
	NON_RANKED(
					FireworkEffect.builder()
									.with(FireworkEffect.Type.BURST)
									.withColor(Colour.CYAN.asBukkitColour())
									.withFade(Colour.DARKER_GREEN.asBukkitColour()),
					FireworkEffect.builder()
									.with(FireworkEffect.Type.BURST)
									.withColor(Colour.CYAN.asBukkitColour(), Colour.LIME.asBukkitColour(), Colour.LIGHT_BLUE.asBukkitColour())
									.withFade(Colour.DARKER_GREEN.asBukkitColour(), Colour.LIME.asBukkitColour())
									.withTrail()
									.withFlicker()
	),
	GOLD(
					1,
					FireworkEffect.builder()
									.with(FireworkEffect.Type.STAR)
									.withColor(Colour.YELLOW.asBukkitColour(), Colour.BANANA.asBukkitColour(), Colour.LIGHTER_ORANGE.asBukkitColour(), Colour.LIGHT_ORANGE.asBukkitColour())
									.withTrail()
									.withFlicker()
	),
	AMETHYST(
					FireworkEffect.builder()
									.with(FireworkEffect.Type.BALL)
									.withColor(Colour.LIGHT_PURPLE.asBukkitColour(), Colour.PURPLE.asBukkitColour())
									.withFade(Colour.PURPLE.asBukkitColour())
									.withTrail()
									.withFlicker()

	),
	EMERALD(
					FireworkEffect.builder()
									.with(FireworkEffect.Type.CREEPER)
									.withColor(Colour.SLIME.asBukkitColour(), Colour.LIME.asBukkitColour(), Colour.SLIME.asBukkitColour(), Colour.DARK_GREEN.asBukkitColour())
									.withFade(Colour.GREEN.asBukkitColour())
									.withFlicker()
	),
	DIAMOND(
					FireworkEffect.builder()
									.with(FireworkEffect.Type.BALL_LARGE)
									.withColor(Colour.SKY_BLUE.asBukkitColour(), Colour.DARK_BLUE.asBukkitColour(), Colour.CYAN.asBukkitColour(), Color.AQUA, Colour.SKY_BLUE.asBukkitColour(), Colour.OCEAN_BLUE.asBukkitColour(), Color.AQUA)
									.withFade(Colour.SKY_BLUE.asBukkitColour())
									.withTrail()
									.withFlicker()
	),
	ROYAL_SLUDGE(
					FireworkEffect.builder()
									.with(FireworkEffect.Type.BALL)
									.withColor(Colour.BLOOD_RED.asBukkitColour(), Colour.BLOOD_RED.asBukkitColour(), Color.RED, Colour.MAGENTA.asBukkitColour(), Colour.MAROON.asBukkitColour())
									.withFade(Color.BLACK, Colour.MAROON.asBukkitColour())
									.withTrail()
									.withFlicker()
	),
	SLIME_KING(
					FireworkEffect.builder()
									.with(FireworkEffect.Type.BALL_LARGE)
									.withColor(Colour.LIME.asBukkitColour(), Colour.MINT.asBukkitColour(), Colour.SLIME.asBukkitColour(), Colour.LIME.asBukkitColour(), Colour.GREEN.asBukkitColour())
									.withFade(Colour.SLIME.asBukkitColour(), Colour.GREEN.asBukkitColour(), Colour.MINT.asBukkitColour(), Colour.LIME.asBukkitColour())
									.withTrail()
									.withFlicker()
	);

	private final FireworkEffect[] effects;
	private final int power;

	FireworkType(int power, FireworkEffect.Builder... effects) {
		this.effects = Arrays.stream(effects).map(FireworkEffect.Builder::build).toArray(FireworkEffect[]::new);
		this.power = power;
	}

	FireworkType(FireworkEffect.Builder... effects) {
		this.effects = Arrays.stream(effects).map(FireworkEffect.Builder::build).toArray(FireworkEffect[]::new);
		this.power = 0;
	}

	public FireworkEffect[] getEffects() {
		return effects;
	}

	public int getPower() {
		return power;
	}
}
