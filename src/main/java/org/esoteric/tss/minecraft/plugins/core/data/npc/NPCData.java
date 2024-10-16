package org.esoteric.tss.minecraft.plugins.core.data.npc;

import org.esoteric.tss.minecraft.plugins.core.data.SimpleLocation;
import org.esoteric.tss.minecraft.plugins.core.data.player.Skin;
import org.jetbrains.annotations.NotNull;

public class NPCData {

    private final int id;
    private final Skin skin;
    private final SimpleLocation location;
    private final String destinationWorldName;

    protected NPCData(@NotNull NPCData npcData) {
        this.id = npcData.id;
        this.skin = npcData.skin;
        this.location = npcData.location;
        this.destinationWorldName = npcData.destinationWorldName;
    }

    public int getId() {
        return id;
    }

    public Skin getSkin() {
        return skin;
    }

    public SimpleLocation getSimpleLocation() {
        return location;
    }

    public String getDestinationWorldName() {
        return destinationWorldName;
    }
}
