package net.slqmy.tss_core.datatype.player.survival;

import net.slqmy.tss_core.TSSCorePlugin;
import net.slqmy.tss_core.datatype.player.Message;
import net.slqmy.tss_core.datatype.player.TranslatableItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public enum SkillType {

  COMBAT(
		  new TranslatableItemStack(Material.DIAMOND_SWORD, Message.COMBAT, Message.COMBAT_DESCRIPTION),
		  Material.STONE_SWORD,
		  Message.COMBAT_INFO,
		  Message.COMBAT_TIPS_TRICKS
  ),
  MINING(
		  new TranslatableItemStack(Material.IRON_PICKAXE, Message.MINING, Message.MINING_DESCRIPTION),
		  Material.STONE_PICKAXE,
		  Message.MINING_INFO,
		  Message.MINING_TIPS_TRICKS
  ),
  FORAGING(
		  new TranslatableItemStack(Material.IRON_AXE, Message.FORAGING, Message.FORAGING_DESCRIPTION),
		  Material.STONE_AXE,
		  Message.FORAGING_INFO,
		  Message.FORAGING_TIPS_TRICKS
  ),
  FARMING(
		  new TranslatableItemStack(Material.STONE_HOE, Message.FARMING, Message.FARMING_DESCRIPTION),
		  Material.STONE_HOE,
		  Message.FARMING_INFO,
		  Message.FARMING_TIPS_TRICKS
  ),

  ENCHANTING(
		  new TranslatableItemStack(Material.ENCHANTING_TABLE, Message.ENCHANTING, Message.ENCHANTING_DESCRIPTION),
		  Material.EXPERIENCE_BOTTLE,
		  Message.ENCHANTING_INFO,
		  Message.ENCHANTING_TIPS_TRICKS
  ),
  ALCHEMY(
		  new TranslatableItemStack(Material.BREWING_STAND, Message.ALCHEMY, Message.ALCHEMY_DESCRIPTION),
		  Material.POTION,
		  Message.ALCHEMY_INFO,
		  Message.ALCHEMY_TIPS_TRICKS
  ),
  FISHING(
		  new TranslatableItemStack(Material.FISHING_ROD, Message.FISHING, Message.FISHING_DESCRIPTION),
		  Material.COD,
		  Message.FISHING_INFO,
		  Message.FISHING_TIPS_TRICKS
  ),
  FORGING(
		  new TranslatableItemStack(Material.ANVIL, Message.FORGING, Message.FORGING_DESCRIPTION),
		  Material.SMITHING_TABLE,
		  Message.FORGING_INFO,
		  Message.FORGING_TIPS_TRICKS
  );

  private final TranslatableItemStack displayItem;
  private final Material secondaryDisplayItemMaterial;
  private final Message infoMessage;
  private final Message tipsTricksMessage;

  SkillType(TranslatableItemStack displayItem, Material secondaryDisplayItemMaterial, Message infoMessage, Message tipsTricksMessage) {
	this.displayItem = displayItem;
	this.secondaryDisplayItemMaterial = secondaryDisplayItemMaterial;
	this.infoMessage = infoMessage;
	this.tipsTricksMessage = tipsTricksMessage;
  }

  public ItemStack getDisplayItem(Player player, TSSCorePlugin core) {
	return displayItem.asBukkitItemStack(player, core);
  }

  public Material getSecondaryDisplayItemMaterial() {
	return secondaryDisplayItemMaterial;
  }

  public Message getInfoMessage() {
	return infoMessage;
  }

  public Message getTipsTricksMessage() {
	return tipsTricksMessage;
  }
}
