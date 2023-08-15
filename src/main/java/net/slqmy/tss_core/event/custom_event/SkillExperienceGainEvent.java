package net.slqmy.tss_core.event.custom_event;

import net.slqmy.tss_core.datatype.player.survival.SkillType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SkillExperienceGainEvent extends Event {

  private static final HandlerList HANDLERS = new HandlerList();

  private final Player player;
  private final SkillType skillType;
  private final int gainedExp;
  private final int totalExp;

  public SkillExperienceGainEvent(Player player, SkillType skillType, int gainedExp, int totalExp) {
	this.player = player;
	this.skillType = skillType;
	this.gainedExp = gainedExp;
	this.totalExp = totalExp;
  }

  public static HandlerList getHandlerList() {
	return HANDLERS;
  }

  public Player getPlayer() {
	return player;
  }

  public SkillType getSkillType() {
	return skillType;
  }

  public int getGainedExp() {
	return gainedExp;
  }

  public int getTotalExp() {
	return totalExp;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
	return HANDLERS;
  }
}
