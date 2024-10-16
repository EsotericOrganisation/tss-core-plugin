package org.esoteric.tss.minecraft.plugins.core.event.listeners;

import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.esoteric.tss.minecraft.plugins.core.TSSCorePlugin;
import org.jetbrains.annotations.NotNull;

public class CustomGuiListener implements Listener {

    private final TSSCorePlugin plugin;

    public CustomGuiListener(TSSCorePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCustomGuiInteract(@NotNull InventoryClickEvent event) {
        ItemStack[] items = event.getInventory().getContents();
        ItemStack stack = null;
        for (int i = 0; stack == null && i < items.length; i++) {
            stack = items[i];
        }

        if (stack == null) {
            return;
        }

        ItemMeta meta = stack.getItemMeta();
        if (meta == null) {
            return;
        }

        if (meta.getPersistentDataContainer().get(new NamespacedKey(plugin, "is_custom_gui"), PersistentDataType.BOOLEAN) != null) {
            event.setCancelled(true);
        }
    }
}
