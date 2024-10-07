package org.esoteric_organisation.tss_core_plugin.datatype.npc;

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
