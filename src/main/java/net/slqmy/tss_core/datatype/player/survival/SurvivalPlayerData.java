package net.slqmy.tss_core.datatype.player.survival;

import java.util.ArrayList;
import java.util.List;

public class SurvivalPlayerData {

  private List<Claim> claims = new ArrayList<>();
  private SkillData skillData = new SkillData();

  public SurvivalPlayerData() {

  }

  public List<Claim> getClaims() {
    return claims;
  }

  public SkillData getSkillData() {
    return skillData;
  }

  public void setClaims(List<Claim> claims) {
    this.claims = claims;
  }

  public void setSkillData(SkillData skillData) {
    this.skillData = skillData;
  }
}
