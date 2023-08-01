package net.slqmy.tss_core.event.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.slqmy.tss_core.TSSCore;
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

	private final TSSCore plugin;

	public ConnectionListener(TSSCore plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onJoin(@NotNull PlayerJoinEvent event) {
		Player player = event.getPlayer();

		try {
			PlayerProfile profile = new PlayerProfile(player, plugin);
			plugin.getPlayerManager().addProfile(player, profile);
		} catch (Exception exception) {
			player.kick(Component.text("Sorry, the server was unable to load your data!", TextColor.color(Color.RED.asRGB())));

			DebugUtil.handleException(exception, "An unexpected error occurred while saving the player profile for player " + player.getName() + "! (UUID: " + player.getUniqueId() + ")");
		}
	}

	@EventHandler
	public void onQuit(@NotNull PlayerQuitEvent event) {
		plugin.getPlayerManager().removeProfile(event.getPlayer());
	}
}
