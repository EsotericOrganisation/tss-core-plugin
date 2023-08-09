package net.slqmy.tss_core.manager;

import net.slqmy.tss_core.TSSCorePlugin;
import net.slqmy.tss_core.data.type.player.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {

	private final HashMap<UUID, PlayerProfile> profiles = new HashMap<>();

	public PlayerManager(TSSCorePlugin plugin) {
		long dayInTicks = 20L * 60L * 60L * 24L;
		Bukkit.getScheduler().runTaskTimer(plugin, () -> saveProfiles(), dayInTicks, dayInTicks);
	}

	public HashMap<UUID, PlayerProfile> getProfiles() {
		return profiles;
	}

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

	public void saveProfiles(boolean async) {
		for (PlayerProfile profile : profiles.values()) {
			profile.save(async);
		}
	}

	public void saveProfiles() {
		saveProfiles(true);
	}
}
