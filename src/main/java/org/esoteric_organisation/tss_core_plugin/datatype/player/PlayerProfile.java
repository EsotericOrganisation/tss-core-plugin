package org.esoteric_organisation.tss_core_plugin.datatype.player;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.esoteric_organisation.tss_core_plugin.TSSCorePlugin;
import org.esoteric_organisation.tss_core_plugin.database.collection_name.PlayersCollectionName;
import org.esoteric_organisation.tss_core_plugin.datatype.player.survival.SurvivalPlayerData;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerProfile {

    private TSSCorePlugin plugin;

    private UUID uuid;

    private String rankName;

    private String discordUserID;

    private PlayerPreferences playerPreferences;
    private PlayerStats playerStats;
    private SurvivalPlayerData survivalData;

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

                survivalData = profile.getSurvivalData();
                survivalData.setPlayerUuid(uuid);
            } else {
                playerPreferences = new PlayerPreferences();
                playerStats = new PlayerStats();
                survivalData = new SurvivalPlayerData(uuid, plugin);

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

    public String getDiscordUserID() {
        return discordUserID;
    }

    public void setDiscordUserID(String discordUserID) {
        this.discordUserID = discordUserID;
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

    public SurvivalPlayerData getSurvivalData() {
        return survivalData;
    }

    public void setSurvivalData(SurvivalPlayerData survivalData) {
        this.survivalData = survivalData;
    }

    public void save(boolean async) {
        PlayerProfile profile = this;
        Runnable save = () -> plugin.getDatabase().getCollection(
                PlayersCollectionName.PLAYER_PROFILES,
                (MongoCollection<PlayerProfile> collection) -> collection.replaceOne(Filters.eq("uuid", uuid), profile),
                PlayerProfile.class
        );

        if (async) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, save);
        } else {
            save.run();
        }
    }

    public void save() {
        save(true);
    }
}
