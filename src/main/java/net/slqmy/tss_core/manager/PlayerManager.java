package net.slqmy.tss_core.manager;

import net.slqmy.tss_core.data.type.player.PlayerProfile;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {
	private final HashMap<UUID, PlayerProfile> profiles = new HashMap<>();

	public PlayerProfile getProfile(UUID uuid) {
		return profiles.get(uuid);
	}

	public PlayerProfile getProfile(@NotNull Player player) {
		return getProfile(player.getUniqueId());
	}

	public void addProfile(UUID uuid, PlayerProfile profile) {
		profiles.put(uuid, profile);
	}

	public void addProfile(@NotNull Player player, PlayerProfile profile) {
		profiles.put(player.getUniqueId(), profile);
	}

	public void removeProfile(UUID uuid) {
		profiles.remove(uuid);
	}

	public void removeProfile(@NotNull Player player) {
		profiles.remove(player.getUniqueId());
	}
}
