package net.slqmy.tss_core;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import net.slqmy.tss_core.util.DebugUtil;
import net.slqmy.tss_core.util.LogUtil;
import org.bson.Document;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

public class MongoDB {

	private final String CLUSTER_ADDRESS;
	private final String USERNAME;
	private final String PASSWORD;

	private MongoClientSettings mongoDBClientSettings;

	public MongoDB(@NotNull TSSCore plugin) {
		YamlConfiguration config = (YamlConfiguration) plugin.getConfig();

		CLUSTER_ADDRESS = config.getString("mongodb-cluster-address");
		USERNAME = config.getString("mongodb-username");
		PASSWORD = config.getString("mongodb-password");

		String connectionString = "mongodb+srv://" + USERNAME + ":" + PASSWORD + "@" + CLUSTER_ADDRESS + ".ld5uece.mongodb.net/?retryWrites=true&w=majority";

		ServerApi mongoDBServerAPI = ServerApi.builder()
		                                      .version(ServerApiVersion.V1)
		                                      .build();

		mongoDBClientSettings = MongoClientSettings.builder()
		                                           .applyConnectionString(new ConnectionString(connectionString))
		                                           .serverApi(mongoDBServerAPI)
		                                           .build();

		try (MongoClient mongoClient = MongoClients.create(mongoDBClientSettings)) {
			MongoDatabase database = mongoClient.getDatabase(DatabaseNames.PLAYERS.getName());

			database.runCommand(new Document("ping", 1));
			LogUtil.log("Pinged the database deployment. Successfully connected to MongoDB!");
		} catch (MongoException exception) {
			DebugUtil.handleException(exception, "An unexpected error occurred while connecting to the database!");
		}
	}

	private enum DatabaseNames {
		PLAYERS;

		private String name;

		DatabaseNames() {
			name = name().toLowerCase();
		}

		public String getName() {
			return name;
		}
	}
}
