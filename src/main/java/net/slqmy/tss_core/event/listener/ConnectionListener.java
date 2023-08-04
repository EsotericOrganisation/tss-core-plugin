package net.slqmy.tss_core.event.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import net.slqmy.tss_core.TSSCorePlugin;
import net.slqmy.tss_core.data.Message;
import net.slqmy.tss_core.type.Colour;
import net.slqmy.tss_core.type.PlayerProfile;
import net.slqmy.tss_core.util.DebugUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class ConnectionListener implements Listener {

	private final TSSCorePlugin plugin;

	public ConnectionListener(TSSCorePlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onLogin(@NotNull PlayerLoginEvent event) {
		Player player = event.getPlayer();

		try {
			PlayerProfile profile = new PlayerProfile(player, plugin);
			plugin.getPlayerManager().addProfile(player, profile);
		} catch (Exception exception) {
			String discordLink = "discord.gg/" + plugin.getConfig().getString("discord-server-invite-code");
			TextComponent discordLinkComponent = Component.text(discordLink, Colour.BLURPLE.asTextColour(), TextDecoration.UNDERLINED);

			event.disallow(Result.KICK_OTHER, plugin.getMessageManager().getPlayerMessage(Message.UNABLE_TO_LOAD_DATA, player, discordLinkComponent));

			DebugUtil.handleException("An unexpected error occurred while loading the player profile for player " + player + "!", exception);
		}
	}

	@EventHandler
	public void onJoin(@NotNull PlayerJoinEvent event) {
		plugin.getPacketManager().injectPlayer(event.getPlayer());
	}

	@EventHandler
	public void onQuit(@NotNull PlayerQuitEvent event) {
		Player player = event.getPlayer();

		plugin.getPlayerManager().removeProfile(player);
		plugin.getPacketManager().ejectPlayer(player);
	}
}
