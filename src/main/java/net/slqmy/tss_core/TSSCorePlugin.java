package net.slqmy.tss_core;

import net.slqmy.tss_core.event.listener.ConnectionListener;
import net.slqmy.tss_core.manager.MessageManager;
import net.slqmy.tss_core.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class TSSCorePlugin extends JavaPlugin {

	private MongoDB database;

	private PlayerManager playerManager;

	private MessageManager messageManager;

	public MongoDB getDatabase() {
		return database;
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public MessageManager getMessageManager() {
		return  messageManager;
	}

	@Override
	public void onEnable() {
		getDataFolder().mkdir();

		YamlConfiguration config = (YamlConfiguration) getConfig();

		config.options().copyDefaults();
		saveDefaultConfig();

		database = new MongoDB(this);
		playerManager = new PlayerManager();
		messageManager = new MessageManager(this);

		Bukkit.getPluginManager().registerEvents(new ConnectionListener(this), this);
	}
}
