package net.slqmy.tss_core.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public class LogUtil {

  private final static ConsoleCommandSender CONSOLE = Bukkit.getConsoleSender();

  private final static TextComponent LOG_PREFIX = LegacyComponentSerializer.legacyAmpersand().deserialize("[&a&lTSS&r-&a&lCore&r]");

  public static void log(Component message) {
	CONSOLE.sendMessage(LOG_PREFIX.append(Component.text(" ").append(message)));
  }

  public static void log(String message) {
	log(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
  }
}
