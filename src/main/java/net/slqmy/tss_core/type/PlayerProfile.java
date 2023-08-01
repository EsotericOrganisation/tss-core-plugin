package net.slqmy.tss_core.type;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import net.slqmy.tss_core.TSSCore;
import net.slqmy.tss_core.database.DatabaseName;
import net.slqmy.tss_core.database.collection_name.PlayersCollectionName;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerProfile {

	private final UUID uuid;

	public PlayerProfile(UUID uuid, @NotNull TSSCore plugin) throws MongoException {
		this.uuid = uuid;

		try (MongoClient mongoClient = MongoClients.create(plugin.getDatabase().getClientSettings())) {
			MongoDatabase database = mongoClient.getDatabase(DatabaseName.PLAYERS.getName());
			MongoCollection<Document> playerProfiles = database.getCollection(PlayersCollectionName.PLAYER_PROFILES.getName());

			try (MongoCursor<Document> matches = playerProfiles.find(Filters.eq("uuid", uuid.toString())).cursor()) {
				if (matches.hasNext()) {
					// Load needed data from the database.
				} else {
					Document playerProfile = new Document();
					playerProfile.put("uuid", uuid.toString());

					playerProfiles.insertOne(playerProfile);
				}
			}
		}
	}

	public PlayerProfile(@NotNull Player player, TSSCore plugin) throws MongoException {
		this(player.getUniqueId(), plugin);
	}

	public UUID getUUID() {
		return uuid;
	}
}
