package net.slqmy.tss_core.event.custom_event;

import net.slqmy.tss_core.datatype.player.survival.SkillType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SkillLevelUpEvent extends Event {

  private static final HandlerList HANDLERS = new HandlerList();

  private final Player player;
  private final SkillType skillType;
  private final int oldLevel;
  private final int newLevel;

  public SkillLevelUpEvent(Player player, SkillType skillType, int oldLevel, int newLevel) {
	this.player = player;
	this.skillType = skillType;
	this.oldLevel = oldLevel;
	this.newLevel = newLevel;
  }

  public Player getPlayer() {
	return player;
  }

  public SkillType getSkillType() {
	return skillType;
  }

  public int getOldLevel() {
	return oldLevel;
  }

  public int getNewLevel() {
	return newLevel;
  }

  public static HandlerList getHandlerList() {
	return HANDLERS;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
	return HANDLERS;
  }
}
