package net.slqmy.tss_core.event.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.ClickEvent.Action;
import net.kyori.adventure.text.event.HoverEvent;
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

			discordLinkComponent = discordLinkComponent.clickEvent(ClickEvent.clickEvent(Action.OPEN_URL, "https://www." + discordLink));
			discordLinkComponent = discordLinkComponent.hoverEvent(
							HoverEvent.hoverEvent(
											HoverEvent.Action.SHOW_TEXT,
											plugin.getMessageManager().getPlayerMessage(Message.CLICK_TO_JOIN, player)
							)
			);

			event.disallow(Result.KICK_OTHER, plugin.getMessageManager().getPlayerMessage(Message.UNABLE_TO_LOAD_DATA, player, discordLinkComponent));

			DebugUtil.handleException("An unexpected error occurred while loading the player profile for player " + player + "!", exception);
		}
	}

	@EventHandler
	public void onJoin(@NotNull PlayerJoinEvent event) {
		plugin.getPacketManager().injectPacketListener(event.getPlayer());
	}

	@EventHandler
	public void onQuit(@NotNull PlayerQuitEvent event) {
		Player player = event.getPlayer();

		plugin.getPlayerManager().removeProfile(player);
		plugin.getPacketManager().ejectPacketListener(player);
	}
}
