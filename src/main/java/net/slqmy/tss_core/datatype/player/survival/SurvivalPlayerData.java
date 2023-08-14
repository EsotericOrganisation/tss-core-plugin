package net.slqmy.tss_core.datatype.player.survival;

import net.slqmy.tss_core.TSSCorePlugin;
import net.slqmy.tss_core.event.custom_event.SkillLevelUpEvent;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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

  public Map<String, ArrayList<ClaimedChunk>> getClaims() {
    return claims;
  }

  public Map<String, Integer> getSkillLevels() {
    return skillLevels;
  }

  public void setPlayerUuid(UUID playerUuid) {
    this.playerUuid = playerUuid;
  }

  public void setClaims(Map<String, ArrayList<ClaimedChunk>> claims) {
    this.claims = claims;
  }

  public void setSkillLevels(Map<String, Integer> skillLevels) {
    this.skillLevels = skillLevels;
  }

  public void incrementSkillExperience(@NotNull SkillType skillType, int increaseAmount) {
    int currentExp = skillLevels.get(skillType.name());
    int oldLevel = getLevel(currentExp);

    int newExp = currentExp + increaseAmount;
	skillLevels.put(skillType.name(), newExp);

    int newLevel = getLevel(newExp);
    if (oldLevel != newLevel) {
      Bukkit.getPluginManager().callEvent(
              new SkillLevelUpEvent(
                      Bukkit.getPlayer(playerUuid),
                      skillType,
                      oldLevel,
                      newLevel
              )
      );
    }
  }

  private int getLevel(int experience) {
    double power = Math.pow(Math.sqrt(3D) * Math.sqrt(27D * Math.pow(experience, 2D) + 10000D) - 9D * experience, 1D / 3D);
    return (int) Math.floor(Math.pow(10D, 2D / 3D) / (Math.pow(3D, 1D / 3D) * power) - power / Math.pow(30D, 2D / 3D));
  }

  public void initialiseClaims(@NotNull TSSCorePlugin plugin) {
    for (String worldName : plugin.getSurvivalWorldNames()) {
      claims.put(worldName, new ArrayList<>());
    }
  }

  public void initialiseSkills() {
    for (SkillType skillType : SkillType.values()) {
	  skillLevels.put(skillType.name(), 0);
    }
  }
}
