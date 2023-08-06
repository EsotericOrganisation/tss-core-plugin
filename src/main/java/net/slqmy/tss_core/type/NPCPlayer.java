package net.slqmy.tss_core.type;

import net.minecraft.server.level.ServerPlayer;
import net.slqmy.tss_core.data.type.npc.NPCData;

public class NPCPlayer {
	private final NPCData npcData;
	private final ServerPlayer nmsEntity;

	public NPCPlayer(NPCData npcData, ServerPlayer nmsEntity) {
		this.npcData = npcData;
		this.nmsEntity = nmsEntity;
	}

	public NPCData getNpc() {
		return npcData;
	}

	public ServerPlayer getNmsEntity() {
		return nmsEntity;
	}
}
