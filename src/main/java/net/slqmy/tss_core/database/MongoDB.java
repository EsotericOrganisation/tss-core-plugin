package net.slqmy.tss_core.database;

import com.mongodb.*;
import com.mongodb.client.*;
import net.slqmy.tss_core.TSSCorePlugin;
import net.slqmy.tss_core.util.DebugUtil;
import net.slqmy.tss_core.util.LogUtil;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDB {

	private final String CLUSTER_ADDRESS;
	private final String USERNAME;
	private final String PASSWORD;

	private final MongoClientSettings clientSettings;

	CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
	CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));

	public MongoDB(@NotNull TSSCorePlugin plugin) {
		YamlConfiguration config = (YamlConfiguration) plugin.getConfig();

		CLUSTER_ADDRESS = config.getString("mongodb-cluster-address");
		USERNAME = config.getString("mongodb-username");
		PASSWORD = config.getString("mongodb-password");

		String connectionString = "mongodb+srv://" +
						USERNAME +
						":" +
						PASSWORD +
						"@" +
						CLUSTER_ADDRESS +
						".ld5uece.mongodb.net/?retryWrites=true&w=majority";

		ServerApi mongoDBServerAPI = ServerApi.builder()
						.version(ServerApiVersion.V1)
						.build();

		clientSettings = MongoClientSettings.builder()
						.applyConnectionString(new ConnectionString(connectionString))
						.serverApi(mongoDBServerAPI)
						.uuidRepresentation(UuidRepresentation.STANDARD)
						.build();

		getMongoDatabase(DatabaseName.PLAYERS, (MongoDatabase database) -> {
			database.runCommand(new Document("ping", 1));

			LogUtil.log("Pinged the database deployment. Successfully connected to MongoDB!");
		});
	}

	public void getMongoDatabase(@NotNull DatabaseName databaseName, @NotNull Consumer<MongoDatabase> consumer) {
		try (MongoClient client = MongoClients.create(clientSettings)) {
			consumer.accept(client.getDatabase(databaseName.getName()).withCodecRegistry(pojoCodecRegistry));
		} catch (MongoException exception) {
			DebugUtil.handleException("An unexpected error occurred while connecting to the database!", exception);
		}
	}

	public <C> void getCursor(@NotNull MongoCollection<C> collection, Bson filter, @NotNull Consumer<MongoCursor<C>> consumer) {
		try (MongoCursor<C> cursor = collection.find(filter).cursor()) {
			consumer.accept(cursor);
		}
	}

	public MongoClientSettings getClientSettings() {
		return clientSettings;
	}
}
