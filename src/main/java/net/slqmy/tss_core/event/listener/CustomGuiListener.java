package net.slqmy.tss_core.event.listener;

import net.slqmy.tss_core.TSSCorePlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class CustomGuiListener implements Listener {

  private final TSSCorePlugin plugin;

  public CustomGuiListener(TSSCorePlugin plugin) {
	this.plugin = plugin;
  }

  @EventHandler
  public void onCustomGuiInteract(@NotNull InventoryClickEvent event) {
	if (event.getInventory().getContents()[0].getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "is_custom_gui"), PersistentDataType.BOOLEAN) != null) {
	  event.setCancelled(true);
	}
  }
}
