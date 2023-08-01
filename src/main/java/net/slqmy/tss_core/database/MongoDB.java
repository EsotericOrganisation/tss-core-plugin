package net.slqmy.tss_core;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import net.slqmy.tss_core.TSSCorePlugin;
import net.slqmy.tss_core.util.DebugUtil;
import net.slqmy.tss_core.util.LogUtil;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

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

	public void getMongoDatabase(@NotNull DatabaseName databaseName, @NotNull MongoDatabaseCallback lambda) {
		try (MongoClient client = MongoClients.create(clientSettings)) {
			lambda.execute(client.getDatabase(databaseName.getName()).withCodecRegistry(pojoCodecRegistry));
		} catch (MongoException exception) {
			DebugUtil.handleException(exception, "An unexpected error occurred while connecting to the database!");
			throw new RuntimeException(exception);
		}
	}

	public MongoClientSettings getClientSettings() {
		return clientSettings;
	}

	public interface MongoDatabaseCallback {
		void execute(MongoDatabase database);
	}
}
