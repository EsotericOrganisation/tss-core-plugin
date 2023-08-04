package net.slqmy.tss_core.type;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import net.slqmy.tss_core.TSSCorePlugin;
import net.slqmy.tss_core.data.type.PlayerData;
import net.slqmy.tss_core.database.DatabaseName;
import net.slqmy.tss_core.database.MongoDB;
import net.slqmy.tss_core.database.collection_name.PlayersCollectionName;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerProfile extends PlayerData {

	@BsonCreator
	public PlayerProfile(@BsonProperty UUID uuid) {
		this.uuid = uuid;
	}

	public PlayerProfile(UUID uuid, @NotNull TSSCorePlugin plugin) throws MongoException {
		this.uuid = uuid;

		MongoDB mongoDB = plugin.getDatabase();

		mongoDB.getMongoDatabase(DatabaseName.PLAYERS, (MongoDatabase database) -> {
			MongoCollection<PlayerProfile> playerProfiles = database
							.getCollection(PlayersCollectionName.PLAYER_PROFILES.getName(), PlayerProfile.class);

			mongoDB.getCursor(playerProfiles, Filters.eq("uuid", uuid), (MongoCursor<PlayerProfile> cursor) -> {
				if (cursor.hasNext()) {
					PlayerProfile profile = cursor.next();

					playerPreferences = profile.getPlayerPreferences();
				} else {
					PlayerProfile profile = new PlayerProfile(uuid);

					playerProfiles.insertOne(profile);
				}
			});
		});
	}

	public PlayerProfile(@NotNull Player player, TSSCorePlugin plugin) throws MongoException {
		this(player.getUniqueId(), plugin);
	}
}
