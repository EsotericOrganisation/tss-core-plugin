package net.slqmy.tss_core;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class TSSCore extends JavaPlugin {

	private MongoDB database;

	public MongoDB getDatabase() {
		return database;
	}

	@Override
	public void onEnable() {
		getDataFolder().mkdir();

		YamlConfiguration config = (YamlConfiguration) getConfig();

		config.options().copyDefaults();
		saveDefaultConfig();

		database = new MongoDB(this);
	}
}
