package org.esoteric.tss.minecraft.plugins.core.events.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.esoteric.tss.minecraft.plugins.core.data.npc.NPC;
import org.jetbrains.annotations.NotNull;

public class NPCClickEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final NPC npc;
    private final Player player;

    public NPCClickEvent(NPC npc, Player player) {
        super(true);

        this.npc = npc;
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public NPC getNpc() {
        return npc;
    }

    public Player getPlayer() {
        return player;
    }
}
