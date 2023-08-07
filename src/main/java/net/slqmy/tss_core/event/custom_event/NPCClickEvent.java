package net.slqmy.tss_core.event.custom_event;

import net.slqmy.tss_core.type.NPCPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class NPCClickEvent extends Event {

	private static final HandlerList handlerList = new HandlerList();

	private final NPCPlayer npcPlayer;
	private final Player player;


	public NPCClickEvent(NPCPlayer npcPlayer, Player player) {
		super(true);

		this.npcPlayer = npcPlayer;
		this.player = player;
	}

	public NPCPlayer getNpcPlayer() {
		return npcPlayer;
	}

	public Player getPlayer() {
		return player;
	}

	public HandlerList getHandlerList() {
		return handlerList;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return handlerList;
	}
}
