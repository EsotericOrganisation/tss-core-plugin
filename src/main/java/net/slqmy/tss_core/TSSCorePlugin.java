package net.slqmy.tss_core;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import net.slqmy.tss_core.database.MongoDB;
import net.slqmy.tss_core.event.listener.ConnectionListener;
import net.slqmy.tss_core.event.listener.DimensionChangeListener;
import net.slqmy.tss_core.manager.MessageManager;
import net.slqmy.tss_core.manager.NPCManager;
import net.slqmy.tss_core.manager.PacketManager;
import net.slqmy.tss_core.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class TSSCorePlugin extends JavaPlugin {

	private MessageManager messageManager;
	private MongoDB database;
	private PlayerManager playerManager;
	private PacketManager packetManager;
	private NPCManager npcManager;

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

	@Override
	public void onEnable() {
		getDataFolder().mkdir();

		YamlConfiguration config = (YamlConfiguration) getConfig();

		config.options().copyDefaults();
		saveDefaultConfig();

		messageManager = new MessageManager(this);
		database = new MongoDB(this);
		playerManager = new PlayerManager();
		npcManager = new NPCManager(this);
		packetManager = new PacketManager(this);

		PluginManager pluginManager = Bukkit.getPluginManager();

		pluginManager.registerEvents(new ConnectionListener(this), this);
		pluginManager.registerEvents(new DimensionChangeListener(this), this);

		CommandAPIBukkitConfig commandConfig = new CommandAPIBukkitConfig(this)
						.shouldHookPaperReload(true);

		CommandAPI.onLoad(commandConfig);
		CommandAPI.onEnable();
	}

	@Override
	public void onDisable() {
		CommandAPI.onDisable();
	}
}
