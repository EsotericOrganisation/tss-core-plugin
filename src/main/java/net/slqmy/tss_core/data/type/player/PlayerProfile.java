package net.slqmy.tss_core.data.type.player;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import net.slqmy.tss_core.TSSCorePlugin;
import net.slqmy.tss_core.database.collection_name.PlayersCollectionName;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerProfile {

	private final UUID uuid;

	private PlayerPreferences playerPreferences;
	private PlayerStats playerStats;

	@BsonCreator
	public PlayerProfile(@BsonProperty UUID uuid) {
		this.uuid = uuid;
	}

	public PlayerProfile(UUID uuid, @NotNull TSSCorePlugin plugin) throws MongoException {
		this.uuid = uuid;

		plugin.getDatabase().getCursor(PlayersCollectionName.PLAYER_PROFILES, Filters.eq("uuid", uuid), (MongoCursor<PlayerProfile> cursor, MongoCollection<PlayerProfile> playerProfiles) -> {
			if (cursor.hasNext()) {
				PlayerProfile profile = cursor.next();

				playerPreferences = profile.getPlayerPreferences();
				playerStats = profile.getPlayerStats();
			} else {
				PlayerProfile profile = new PlayerProfile(uuid);
				playerProfiles.insertOne(profile);
			}
		}, PlayerProfile.class);
	}

	public PlayerProfile(@NotNull Player player, TSSCorePlugin plugin) throws MongoException {
		this(player.getUniqueId(), plugin);
	}

	public PlayerPreferences getPlayerPreferences() {
		return playerPreferences;
	}

	public PlayerStats getPlayerStats() {
		return playerStats;
	}
}
