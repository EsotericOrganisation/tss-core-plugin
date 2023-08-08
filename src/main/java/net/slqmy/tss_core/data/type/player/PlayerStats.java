package net.slqmy.tss_core.data.type.player;

public class PlayerStats {

	private int joinCount;

	public PlayerStats() {
		joinCount = 0;
	}

	public int getJoinCount() {
		return joinCount;
	}

	public void incrementJoinCount() {
		joinCount++;
	}
}
