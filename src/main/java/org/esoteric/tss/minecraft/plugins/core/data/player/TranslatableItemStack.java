package org.esoteric.tss.minecraft.plugins.core.data.player;

import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.esoteric.tss.minecraft.plugins.core.TSSCorePlugin;
import org.esoteric.tss.minecraft.plugins.core.managers.MessageManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TranslatableItemStack extends ItemStack {

    private final Message itemName;
    private final Message itemLore;

    public TranslatableItemStack(Material material, int amount, Message itemName, Message itemLore) {
        super(material, amount);

        this.itemName = itemName;
        this.itemLore = itemLore;
    }

    public TranslatableItemStack(Material material, Message itemName, Message itemLore) {
        this(material, 1, itemName, itemLore);
    }

    public ItemStack asBukkitItemStack(Player player, @NotNull TSSCorePlugin plugin) {
        MessageManager messageManager = plugin.getMessageManager();

        ItemStack copy = this.clone();
        ItemMeta meta = copy.getItemMeta();

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        meta.displayName(messageManager.getPlayerMessage(itemName, player).decoration(TextDecoration.ITALIC, false));
        meta.lore(List.of(messageManager.getPlayerMessage(itemLore, player).decoration(TextDecoration.ITALIC, false)));

        copy.setItemMeta(meta);

        return copy;
    }
}
