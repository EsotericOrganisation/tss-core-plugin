package net.slqmy.tss_core;

import net.slqmy.tss_core.event.listener.ConnectionListener;
import net.slqmy.tss_core.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class TSSCore extends JavaPlugin {

	private MongoDB database;

	private PlayerManager playerManager;

	public MongoDB getDatabase() {
		return database;
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	@Override
	public void onEnable() {
		getDataFolder().mkdir();

		YamlConfiguration config = (YamlConfiguration) getConfig();

		config.options().copyDefaults();
		saveDefaultConfig();

		database = new MongoDB(this);
		playerManager = new PlayerManager();

		Bukkit.getPluginManager().registerEvents(new ConnectionListener(this), this);
	}
}
