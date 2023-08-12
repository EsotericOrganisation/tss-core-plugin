package net.slqmy.tss_core.datatype.player.survival;

import java.util.ArrayList;

public class SurvivalPlayerData {

  private ArrayList<int[]> claims = new ArrayList<>();
  private SkillData skillData;

  public SurvivalPlayerData() {

  }

  public ArrayList<int[]> getClaims() {
    return claims;
  }

  public SkillData getSkillData() {
    return skillData;
  }

  public void setClaims(ArrayList<int[]> claims) {
    this.claims = claims;
  }

  public void setSkillData(SkillData skillData) {
    this.skillData = skillData;
  }
}
