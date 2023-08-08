package net.slqmy.tss_core.data.type.player;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import net.slqmy.tss_core.TSSCorePlugin;
import net.slqmy.tss_core.database.collection_name.PlayersCollectionName;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerProfile {

	private UUID uuid;

	private String rankName;
	private PlayerPreferences playerPreferences;
	private PlayerStats playerStats;

	public PlayerProfile() {

	}

	public PlayerProfile(UUID uuid, @NotNull TSSCorePlugin plugin) throws MongoException {
		this.uuid = uuid;

		plugin.getDatabase().getCursor(PlayersCollectionName.PLAYER_PROFILES, Filters.eq("uuid", uuid), (MongoCursor<PlayerProfile> cursor, MongoCollection<PlayerProfile> playerProfiles) -> {
			if (cursor.hasNext()) {
				PlayerProfile profile = cursor.next();

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

	public String getRankName() {
		return rankName;
	}

	public PlayerPreferences getPlayerPreferences() {
		return playerPreferences;
	}

	public PlayerStats getPlayerStats() {
		return playerStats;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
	}
}
