package org.esoteric_organisation.tss_core_plugin;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.esoteric_organisation.tss_core_plugin.database.MongoDB;
import org.esoteric_organisation.tss_core_plugin.event.listener.ConnectionListener;
import org.esoteric_organisation.tss_core_plugin.event.listener.CustomGuiListener;
import org.esoteric_organisation.tss_core_plugin.event.listener.DimensionChangeListener;
import org.esoteric_organisation.tss_core_plugin.manager.*;

import java.util.List;

public final class TSSCorePlugin extends JavaPlugin {

    private FileManager fileManager;
    private MessageManager messageManager;
    private MongoDB database;
    private PlayerManager playerManager;
    private PacketManager packetManager;
    private NPCManager npcManager;

    private List<String> survivalWorldNames;

    public FileManager getFileManager() {
        return fileManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public MongoDB getDatabase() {
        return database;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public PacketManager getPacketManager() {
        return packetManager;
    }

    public NPCManager getNpcManager() {
        return npcManager;
    }

    public List<String> getSurvivalWorldNames() {
        return survivalWorldNames;
    }

    @Override
    public void onEnable() {
        getDataFolder().mkdir();

        YamlConfiguration config = (YamlConfiguration) getConfig();

        config.options().copyDefaults();
        saveDefaultConfig();

        survivalWorldNames = config.getStringList("survival-world-names");

        fileManager = new FileManager(this);
        messageManager = new MessageManager(this);
        database = new MongoDB(this);
        playerManager = new PlayerManager(this);
        npcManager = new NPCManager(this);
        packetManager = new PacketManager(this);

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new ConnectionListener(this), this);
        pluginManager.registerEvents(new DimensionChangeListener(this), this);
        pluginManager.registerEvents(new CustomGuiListener(this), this);

        CommandAPIBukkitConfig commandConfig = new CommandAPIBukkitConfig(this)
                .shouldHookPaperReload(true);

        CommandAPI.onLoad(commandConfig);
        CommandAPI.onEnable();
    }

    @Override
    public void onDisable() {
        if (playerManager != null) {
            playerManager.saveProfiles(false);
        }

        CommandAPI.onDisable();
    }
}
