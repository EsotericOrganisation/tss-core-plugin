package org.esoteric_organisation.tss_core_plugin.datatype.player;

public class PlayerStats {

  private int joinCount;

  public PlayerStats() {

  }

  public int getJoinCount() {
	return joinCount;
  }

  public void setJoinCount(int joinCount) {
	this.joinCount = joinCount;
  }

  public void incrementJoinCount() {
	joinCount++;
  }
}
