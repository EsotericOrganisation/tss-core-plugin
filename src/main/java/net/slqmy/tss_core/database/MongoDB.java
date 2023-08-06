package net.slqmy.tss_core.database;

import com.mongodb.*;
import com.mongodb.client.*;
import net.slqmy.tss_core.TSSCorePlugin;
import net.slqmy.tss_core.data.Message;
import net.slqmy.tss_core.database.collection_name.CollectionName;
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

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDB {

	private final TSSCorePlugin plugin;

	private final MongoClientSettings clientSettings;

	private final CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
	private final CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));

	public MongoDB(@NotNull TSSCorePlugin plugin) {
		this.plugin = plugin;
		YamlConfiguration config = (YamlConfiguration) plugin.getConfig();

		String clusterAddress = config.getString("mongodb-cluster-address");
		String username = config.getString("mongodb-username");
		String password = config.getString("mongodb-password");

		String connectionString = "mongodb+srv://" +
						username +
						":" +
						password +
						"@" +
						clusterAddress +
						".ld5uece.mongodb.net/?retryWrites=true&w=majority";

		ServerApi api = ServerApi.builder()
						.version(ServerApiVersion.V1)
						.build();

		clientSettings = MongoClientSettings.builder()
						.applyConnectionString(new ConnectionString(connectionString))
						.serverApi(api)
						.uuidRepresentation(UuidRepresentation.STANDARD)
						.build();

		getMongoDatabase(DatabaseName.PLAYERS, (MongoDatabase database) -> {
			database.runCommand(new Document("ping", 1));
			LogUtil.log(plugin.getMessageManager().getMessage(Message.SUCCESSFUL_DATABASE_CONNECTION));
		});
	}

	public void getMongoDatabase(@NotNull DatabaseName databaseName, @NotNull Consumer<MongoDatabase> consumer) {
		String name = databaseName.getName();

		try (MongoClient client = MongoClients.create(clientSettings)) {
			consumer.accept(client.getDatabase(name).withCodecRegistry(pojoCodecRegistry));
		} catch (MongoException exception) {
			DebugUtil.handleException(plugin.getMessageManager().getMessage(Message.DATABASE_CONNECTION_FAILED, name), exception);
		}
	}

	public <C> void getCursor(@NotNull CollectionName collectionName, Bson filter, @NotNull BiConsumer<MongoCursor<C>, MongoCollection<C>> consumer, Class<C> documentClass) {
		getMongoDatabase(collectionName.getDatabaseName(), (MongoDatabase database) -> {
			MongoCollection<C> collection = database.getCollection(collectionName.getName(), documentClass);

			try (MongoCursor<C> cursor = collection.find(filter).cursor()) {
				consumer.accept(cursor, collection);
			}
		});
	}
}
