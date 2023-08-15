package net.slqmy.tss_core.datatype.player.survival;

import net.slqmy.tss_core.TSSCorePlugin;
import net.slqmy.tss_core.datatype.player.Message;
import net.slqmy.tss_core.datatype.player.TranslatableItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public enum SkillType {

  COMBAT(new TranslatableItemStack(Material.DIAMOND_SWORD, Message.COMBAT_ITEM_DISPLAY_NAME, Message.COMBAT_ITEM_DESCRIPTION)),
  MINING(new TranslatableItemStack(Material.IRON_PICKAXE, Message.MINING_ITEM_DISPLAY_NAME, Message.MINING_ITEM_DESCRIPTION)),
  FORAGING(new TranslatableItemStack(Material.IRON_AXE, Message.FORAGING_ITEM_DISPLAY_NAME, Message.FORAGING_ITEM_DESCRIPTION)),
  FARMING(new TranslatableItemStack(Material.STONE_HOE, Message.FARMING_ITEM_DISPLAY_NAME, Message.FARMING_ITEM_DESCRIPTION)),

  ENCHANTING(new TranslatableItemStack(Material.ENCHANTING_TABLE, Message.ENCHANTING_ITEM_DISPLAY_NAME, Message.ENCHANTING_ITEM_DESCRIPTION)),
  ALCHEMY(new TranslatableItemStack(Material.BREWING_STAND, Message.ALCHEMY_ITEM_DISPLAY_NAME, Message.ALCHEMY_ITEM_DESCRIPTION)),
  FISHING(new TranslatableItemStack(Material.FISHING_ROD, Message.FISHING_ITEM_DISPLAY_NAME, Message.FISHING_ITEM_DESCRIPTION)),
  FORGING(new TranslatableItemStack(Material.ANVIL, Message.FORGING_ITEM_DISPLAY_NAME, Message.FORGING_ITEM_DESCRIPTION));

  private final TranslatableItemStack displayItem;

  SkillType(TranslatableItemStack displayItem) {
	this.displayItem = displayItem;
  }

  public ItemStack getDisplayItem(Player player, TSSCorePlugin core) {
	return displayItem.asBukkitItemStack(player, core);
  }
}
