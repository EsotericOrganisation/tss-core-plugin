package org.esoteric.tss.minecraft.plugins.core.data.npc;

import net.minecraft.server.level.ServerPlayer;

public class NPC extends NPCData {

    private final ServerPlayer nmsEntity;

    public NPC(NPCData npcData, ServerPlayer nmsEntity) {
        super(npcData);
        this.nmsEntity = nmsEntity;
    }

    public ServerPlayer getNmsEntity() {
        return nmsEntity;
    }
}
