package net.slqmy.tss_core.datatype.player.survival;

public class Claim {

  private int chunkX;
  private int chunkZ;

  public Claim() {

  }

  public Claim(int chunkX, int chunkZ) {
	this.chunkX = chunkX;
	this.chunkZ = chunkZ;
  }

  public int getChunkX() {
	return chunkX;
  }

  public int getChunkZ() {
	return chunkZ;
  }

  public void setChunkX(int chunkX) {
	this.chunkX = chunkX;
  }

  public void setChunkZ(int chunkZ) {
	this.chunkZ = chunkZ;
  }
}
