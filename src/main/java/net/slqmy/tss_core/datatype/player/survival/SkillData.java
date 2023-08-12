package net.slqmy.tss_core.datatype.player.survival;

public class SkillData {

  private int combatSkillExperience;
  private int miningSkillExperience;
  private int foragingSkillExperience;
  private int farmingSkillExperience;

  private int alchemySkillExperience;
  private int enchantingSkillExperience;
  private int fishingSkillExperience;
  private int forgingSkillExperience;

  public int getCombatSkillExperience() {
	return combatSkillExperience;
  }

  public int getMiningSkillExperience() {
	return miningSkillExperience;
  }

  public int getForagingSkillExperience() {
	return foragingSkillExperience;
  }

  public int getFarmingSkillExperience() {
	return farmingSkillExperience;
  }

  public int getAlchemySkillExperience() {
	return alchemySkillExperience;
  }

  public int getEnchantingSkillExperience() {
	return enchantingSkillExperience;
  }

  public int getFishingSkillExperience() {
	return fishingSkillExperience;
  }

  public int getForgingSkillExperience() {
	return forgingSkillExperience;
  }

  public void setCombatSkillExperience(int combatSkillExperience) {
	this.combatSkillExperience = combatSkillExperience;
  }

  public void setMiningSkillExperience(int miningSkillExperience) {
	this.miningSkillExperience = miningSkillExperience;
  }

  public void setForagingSkillExperience(int foragingSkillExperience) {
	this.foragingSkillExperience = foragingSkillExperience;
  }

  public void setFarmingSkillExperience(int farmingSkillExperience) {
	this.farmingSkillExperience = farmingSkillExperience;
  }

  public void setEnchantingSkillExperience(int enchantingSkillExperience) {
	this.enchantingSkillExperience = enchantingSkillExperience;
  }

  public void setAlchemySkillExperience(int alchemySkillExperience) {
	this.alchemySkillExperience = alchemySkillExperience;
  }

  public void setFishingSkillExperience(int fishingSkillExperience) {
	this.fishingSkillExperience = fishingSkillExperience;
  }

  public void setForgingSkillExperience(int forgingSkillExperience) {
	this.forgingSkillExperience = forgingSkillExperience;
  }

  public void incrementCombatSkillExperience(int increaseAmount) {
	combatSkillExperience += increaseAmount;
  }

  public void incrementMiningSkillExperience(int increaseAmount) {
	miningSkillExperience += increaseAmount;
  }
  public void incrementForagingSkillExperience(int increaseAmount) {
	foragingSkillExperience += increaseAmount;
  }
  public void incrementFarmingSkillExperience(int increaseAmount) {
	farmingSkillExperience += increaseAmount;
  }
  public void incrementEnchantingSkillExperience(int increaseAmount) {
	enchantingSkillExperience += increaseAmount;
  }
  public void incrementAlchemySkillExperience(int increaseAmount) {
	alchemySkillExperience += increaseAmount;
  }
  public void incrementFishingSkillExperience(int increaseAmount) {
	fishingSkillExperience += increaseAmount;
  }
  public void incrementForgingSkillExperience(int increaseAmount) {
	forgingSkillExperience += increaseAmount;
  }
}
