package net.slqmy.tss_core.event.listener;

import net.slqmy.tss_core.TSSCorePlugin;
import net.slqmy.tss_core.data.Message;
import net.slqmy.tss_core.type.PlayerProfile;
import net.slqmy.tss_core.util.DebugUtil;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class ConnectionListener implements Listener {

	private final TSSCorePlugin plugin;

	public ConnectionListener(TSSCorePlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onJoin(@NotNull PlayerJoinEvent event) {
		Player player = event.getPlayer();

		try {
			PlayerProfile profile = new PlayerProfile(player, plugin);
			plugin.getPlayerManager().addProfile(player, profile);
		} catch (Exception exception) {
			player.kick(plugin.getMessageManager().getPlayerMessage(Message.UNABLE_TO_LOAD_DATA, player));

			DebugUtil.handleException(exception, "An unexpected error occurred while saving the player profile for player " + player.getName() + "! (UUID: " + player.getUniqueId() + ")");
		}
	}

	@EventHandler
	public void onQuit(@NotNull PlayerQuitEvent event) {
		plugin.getPlayerManager().removeProfile(event.getPlayer());
	}
}
