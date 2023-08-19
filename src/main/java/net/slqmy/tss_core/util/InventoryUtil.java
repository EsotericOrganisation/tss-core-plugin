package net.slqmy.tss_core.util;

import net.slqmy.tss_core.TSSCorePlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class InventoryUtil {

  public static void makeStatic(@NotNull Inventory inventory, TSSCorePlugin plugin) {
	ItemStack[] items = inventory.getContents();

	ItemStack itemStack = null;
	for (int i = 0; itemStack == null && i < items.length; i++) {
	  itemStack = items[i];
	}

	if (itemStack == null) {
	  throw new IndexOutOfBoundsException("Inventory must contain at least one item!");
	}

	ItemMeta meta = itemStack.getItemMeta();
	meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "is_custom_gui"), PersistentDataType.BOOLEAN, true);
	itemStack.setItemMeta(meta);
  }
}
