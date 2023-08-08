package net.slqmy.tss_core.data.type;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class Rank {

	private String name;
	private String displayName;

	private List<Permission> permissions;

	private FireworkType fireworkType;

	public Rank() {

	}

	public String getName() {
		return name;
	}

	public @NotNull TextComponent getDisplayName() {
		return (TextComponent) MiniMessage.miniMessage().deserialize(displayName);
	}

	public List<Permission> getPermissions() {
		if (permissions != null) {
			return permissions;
		}

		return new ArrayList<>();
	}

	public FireworkType getFireworkType() {
		return fireworkType;
	}
}
