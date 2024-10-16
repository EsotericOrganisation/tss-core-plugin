package org.esoteric.tss.minecraft.plugins.core.event.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.esoteric.tss.minecraft.plugins.core.TSSCorePlugin;
import org.jetbrains.annotations.NotNull;

public class DimensionChangeListener implements Listener {

    private final TSSCorePlugin plugin;

    public DimensionChangeListener(TSSCorePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTeleport(@NotNull PlayerTeleportEvent event) {
        updateNpcs(event.getPlayer());
    }

    @EventHandler
    public void onRespawn(@NotNull PlayerRespawnEvent event) {
        updateNpcs(event.getPlayer());
    }

    private void updateNpcs(Player player) {
        Bukkit.getScheduler().runTaskLater(plugin, () ->
                        plugin.getNpcManager().updateNpcs(player)
                , 1);
    }
}
