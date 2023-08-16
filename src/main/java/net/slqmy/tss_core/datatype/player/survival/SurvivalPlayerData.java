package net.slqmy.tss_core.datatype.player.survival;

import net.slqmy.tss_core.TSSCorePlugin;
import net.slqmy.tss_core.event.custom_event.SkillExperienceGainEvent;
import net.slqmy.tss_core.event.custom_event.SkillLevelUpEvent;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SurvivalPlayerData {

  @BsonIgnore
  private UUID playerUuid;

  private Map<String, ArrayList<ClaimedChunk>> claims = new HashMap<>();
  private Map<String, Integer> skillLevels = new HashMap<>();

  public SurvivalPlayerData() {

  }

  public SurvivalPlayerData(UUID playerUuid, @NotNull TSSCorePlugin plugin) {
	this.playerUuid = playerUuid;

	initialiseClaims(plugin);
	initialiseSkills();
  }

  public static int levelToExperience(int level) {
	return 50 * level * level * level + 50 * level;
  }

  public static int experienceToLevel(int experience) {
	double power = Math.pow(1.7320508075688772D * Math.sqrt(27.0D * Math.pow(experience, 2.0D) + 10000.0D) - 9.0D * experience, 1.0D / 3.0D);
	return (int) Math.floor(Math.pow(10.0D, 2.0D / 3.0D) / (Math.pow(3.0D, 1.0D / 3.0D) * power) - power / Math.pow(30.0D, 2.0D / 3.0D));
  }

  private void initialiseClaims(@NotNull TSSCorePlugin plugin) {
	for (String worldName : plugin.getSurvivalWorldNames()) {
	  claims.put(worldName, new ArrayList<>());
	}
  }

  private void initialiseSkills() {
	for (SkillType skillType : SkillType.values()) {
	  skillLevels.put(skillType.name(), 0);
	}
  }

  public Map<String, ArrayList<ClaimedChunk>> getClaims() {
	return claims;
  }

  public void setClaims(Map<String, ArrayList<ClaimedChunk>> claims) {
	this.claims = claims;
  }

  public Map<String, Integer> getSkillLevels() {
	return skillLevels;
  }

  public void setSkillLevels(Map<String, Integer> skillLevels) {
	this.skillLevels = skillLevels;
  }

  public void setPlayerUuid(UUID playerUuid) {
	this.playerUuid = playerUuid;
  }

  public int getSkillExperience(@NotNull SkillType skillType) {
	return skillLevels.get(skillType.name());
  }

  public void incrementSkillExperience(@NotNull SkillType skillType, int increaseAmount) {
	int currentExp = skillLevels.get(skillType.name());
	int oldLevel = experienceToLevel(currentExp);

	int newExp = currentExp + increaseAmount;
	skillLevels.put(skillType.name(), newExp);

	Player player = Bukkit.getPlayer(playerUuid);

	PluginManager pluginManager = Bukkit.getPluginManager();
	pluginManager.callEvent(
			new SkillExperienceGainEvent(
					player,
					skillType,
					increaseAmount,
					newExp
			)
	);

	int newLevel = experienceToLevel(newExp);
	if (oldLevel != newLevel) {
	  pluginManager.callEvent(
			  new SkillLevelUpEvent(
					  player,
					  skillType,
					  oldLevel,
					  newLevel
			  )
	  );
	}
  }
}
