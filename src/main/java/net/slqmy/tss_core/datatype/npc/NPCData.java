package net.slqmy.tss_core.datatype.npc;

import net.slqmy.tss_core.datatype.SimpleLocation;
import net.slqmy.tss_core.datatype.player.Skin;

public class NPCData {
	private int id;
	private Skin skin;
	private SimpleLocation simpleLocation;
	private String destinationWorldName;

	public int getId() {
		return id;
	}

	public Skin getSkin() {
		return skin;
	}

	public SimpleLocation getSimpleLocation() {
		return simpleLocation;
	}

	public String getDestinationWorldName() {
		return destinationWorldName;
	}
}
