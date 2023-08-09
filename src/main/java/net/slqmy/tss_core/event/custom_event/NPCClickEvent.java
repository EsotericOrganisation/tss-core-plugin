package net.slqmy.tss_core.event.custom_event;

import net.slqmy.tss_core.type.NPCPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class NPCClickEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();

	private final NPCPlayer npcPlayer;
	private final Player player;

	public NPCClickEvent(NPCPlayer npcPlayer, Player player) {
		super(true);

		this.npcPlayer = npcPlayer;
		this.player = player;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLERS;
	}

	public NPCPlayer getNpcPlayer() {
		return npcPlayer;
	}

	public Player getPlayer() {
		return player;
	}
}
