package net.slqmy.tss_core.data.type.player;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import net.slqmy.tss_core.TSSCorePlugin;
import net.slqmy.tss_core.database.collection_name.PlayersCollectionName;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerProfile {

	private TSSCorePlugin plugin;

	private UUID uuid;

	private String rankName;

	private PlayerPreferences playerPreferences;
	private PlayerStats playerStats;

	public PlayerProfile() {

	}

	public PlayerProfile(UUID uuid, @NotNull TSSCorePlugin plugin) throws MongoException {
		this.plugin = plugin;

		this.uuid = uuid;

		plugin.getDatabase().getCursor(PlayersCollectionName.PLAYER_PROFILES, Filters.eq("uuid", uuid), (MongoCursor<PlayerProfile> cursor, MongoCollection<PlayerProfile> playerProfiles) -> {
			if (cursor.hasNext()) {
				PlayerProfile profile = cursor.next();

				rankName = profile.getRankName();

				playerPreferences = profile.getPlayerPreferences();
				playerStats = profile.getPlayerStats();
			} else {
				playerPreferences = new PlayerPreferences();
				playerStats = new PlayerStats();

				playerProfiles.insertOne(this);
			}
		}, PlayerProfile.class);
	}

	public PlayerProfile(@NotNull Player player, TSSCorePlugin plugin) throws MongoException {
		this(player.getUniqueId(), plugin);
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getRankName() {
		return rankName;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
	}

	public PlayerPreferences getPlayerPreferences() {
		return playerPreferences;
	}

	public void setPlayerPreferences(PlayerPreferences playerPreferences) {
		this.playerPreferences = playerPreferences;
	}

	public PlayerStats getPlayerStats() {
		return playerStats;
	}

	public void setPlayerStats(PlayerStats playerStats) {
		this.playerStats = playerStats;
	}

	public void save(boolean async) {
		PlayerProfile profile = this;
		Runnable save = () -> plugin.getDatabase().getCollection(
						PlayersCollectionName.PLAYER_PROFILES,
						(MongoCollection<PlayerProfile> collection) -> collection.replaceOne(Filters.eq("uuid", uuid), profile),
						PlayerProfile.class
		);

		BukkitScheduler scheduler = Bukkit.getScheduler();

		if (async) {
			scheduler.runTaskAsynchronously(plugin, save);
		} else {
			save.run();
		}
	}

	public void save() {
		save(true);
	}
}
