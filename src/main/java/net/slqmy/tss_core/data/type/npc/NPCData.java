package net.slqmy.tss_core.data.type.npc;

import net.slqmy.tss_core.data.type.SimpleLocation;
import net.slqmy.tss_core.data.type.Skin;

public class NPCData {
	private int id;
	private Skin skin;
	private SimpleLocation location;

	public int getId() {
		return id;
	}

	public Skin getSkin() {
		return skin;
	}

	public SimpleLocation getSimpleLocation() {
		return location;
	}
}
