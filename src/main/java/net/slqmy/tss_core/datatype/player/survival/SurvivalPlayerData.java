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

  public SurvivalPlayerData(final UUID playerUuid, @NotNull final TSSCorePlugin plugin) {
	this.playerUuid = playerUuid;

	this.initialiseClaims(plugin);
	this.initialiseSkills();
  }

  private void initialiseClaims(@NotNull final TSSCorePlugin plugin) {
	for (final String worldName : plugin.getSurvivalWorldNames()) {
	  this.claims.put(worldName, new ArrayList<>());
	}
  }

  private void initialiseSkills() {
	for (final SkillType skillType : SkillType.values()) {
	  this.skillLevels.put(skillType.name(), 0);
	}
  }

  public Map<String, ArrayList<ClaimedChunk>> getClaims() {
	return this.claims;
  }

  public void setClaims(final Map<String, ArrayList<ClaimedChunk>> claims) {
	this.claims = claims;
  }

  public Map<String, Integer> getSkillLevels() {
	return this.skillLevels;
  }

  public void setSkillLevels(final Map<String, Integer> skillLevels) {
	this.skillLevels = skillLevels;
  }

  public void setPlayerUuid(final UUID playerUuid) {
	this.playerUuid = playerUuid;
  }

  public int getSkillExperience(@NotNull final SkillType skillType) {
	return this.skillLevels.get(skillType.name());
  }

  public void incrementSkillExperience(@NotNull final SkillType skillType, final int increaseAmount) {
	final int currentExp = this.skillLevels.get(skillType.name());
	final int oldLevel = SurvivalPlayerData.experienceToLevel(currentExp);

	final int newExp = currentExp + increaseAmount;
	this.skillLevels.put(skillType.name(), newExp);

	final Player player = Bukkit.getPlayer(this.playerUuid);

	final PluginManager pluginManager = Bukkit.getPluginManager();
	pluginManager.callEvent(
			new SkillExperienceGainEvent(
					player,
					skillType,
					increaseAmount,
					newExp
			)
	);

	final int newLevel = SurvivalPlayerData.experienceToLevel(newExp);
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

  public static int experienceToLevel(final int experience) {
	final double power = Math.pow(1.7320508075688772D * Math.sqrt(27.0D * Math.pow(experience, 2.0D) + 10000.0D) - 9.0D * experience, 1.0D / 3.0D);
	return (int) Math.floor(Math.pow(10.0D, 2.0D / 3.0D) / (Math.pow(3.0D, 1.0D / 3.0D) * power) - power / Math.pow(30.0D, 2.0D / 3.0D));
  }
}
