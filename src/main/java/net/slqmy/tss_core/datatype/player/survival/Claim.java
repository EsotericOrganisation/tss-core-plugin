package net.slqmy.tss_core.datatype.player.survival;

public class Claim {
  private String worldName;

  private int chunkX;
  private int chunkZ;

  public Claim() {

  }

  public Claim(String worldName, int chunkX, int chunkZ) {
	this.worldName = worldName;
	this.chunkX = chunkX;
	this.chunkZ = chunkZ;
  }

  public String getWorldName() {
	return worldName;
  }

  public int getChunkX() {
	return chunkX;
  }

  public int getChunkZ() {
	return chunkZ;
  }

  public void setWorldName(String worldName) {
	this.worldName = worldName;
  }

  public void setChunkX(int chunkX) {
	this.chunkX = chunkX;
  }

  public void setChunkZ(int chunkZ) {
	this.chunkZ = chunkZ;
  }
}
