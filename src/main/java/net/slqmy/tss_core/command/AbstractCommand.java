package net.slqmy.tss_core.command;

import net.slqmy.tss_core.TSSCorePlugin;
import net.slqmy.tss_core.data.Message;
import net.slqmy.tss_core.manager.MessageManager;
import net.slqmy.tss_core.util.LogUtil;
import net.slqmy.tss_core.util.ReflectUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class AbstractCommand extends Command {
	private static final CommandMap COMMAND_MAP = (CommandMap) ReflectUtil.getFieldValue(Bukkit.getServer(), "commandMap");

	private final TSSCorePlugin plugin;

	private final String usage;

	private boolean isConsoleUsable;
	private boolean isCommandBlockUsable;
	private boolean isPlayerUsable;

	protected AbstractCommand(
					TSSCorePlugin plugin,
					@NotNull final String name,
					String description,
					String usage,
					String[] aliases,
					String permission,
					boolean isConsoleUsable,
					boolean isCommandBlockUsable,
					boolean isPlayerUsable
	) {
		super(name);

		this.plugin = plugin;

		if (description != null) {
			setDescription(description);
		}

		this.usage = usage;

		if (aliases != null) {
			setAliases(Arrays.asList(aliases));
		}

		if (permission != null) {
			setPermission(permission);
		}

		this.isPlayerUsable = isPlayerUsable;
		this.isConsoleUsable = isConsoleUsable;
		this.isCommandBlockUsable = isCommandBlockUsable;

		COMMAND_MAP.register(name, this);
	}

	protected AbstractCommand(
					TSSCorePlugin plugin,
					@NotNull final String name,
					String description,
					String usage,
					String[] aliases,
					String permission,
					boolean isConsoleUsable,
					boolean isCommandBlockUsable) {
		this(
						plugin,
						name,
						description,
						usage,
						aliases,
						permission,
						isConsoleUsable,
						isCommandBlockUsable,
						true);
	}

	protected AbstractCommand(
					TSSCorePlugin plugin,
					@NotNull final String name,
					String description,
					String usage,
					String[] aliases,
					String permission,
					boolean isConsoleUsable) {
		this(
						plugin,
						name,
						description,
						usage,
						aliases,
						permission,
						isConsoleUsable,
						false);
	}

	protected AbstractCommand(
					TSSCorePlugin plugin,
					@NotNull final String name,
					String description,
					String usage,
					String[] aliases,
					String permission) {
		this(
						plugin,
						name,
						description,
						usage,
						aliases,
						permission,
						false);
	}

	protected AbstractCommand(
					TSSCorePlugin plugin,
					@NotNull final String name,
					String description,
					String usage,
					String[] aliases) {
		this(
						plugin,
						name,
						description,
						usage,
						aliases,
						null);
	}

	protected AbstractCommand(
					TSSCorePlugin plugin,
					@NotNull final String name,
					String description,
					String usage) {
		this(
						plugin,
						name,
						description,
						usage,
						null);
	}

	protected AbstractCommand(
					TSSCorePlugin plugin,
					@NotNull final String name,
					String description) {
		this(
						plugin,
						name,
						description,
						null);
	}

	protected AbstractCommand(
					TSSCorePlugin plugin,
					@NotNull final String name) {
		this(
						plugin,
						name,
						null);
	}

	protected boolean execute(Player sender, String[] args) {
		return true;
	}

	protected boolean execute(ConsoleCommandSender sender, String[] args) {
		return true;
	}

	protected boolean execute(BlockCommandSender sender, String[] args) {
		return true;
	}

	protected ArrayList<String> tabComplete(Player sender, String[] args) {
		return new ArrayList<>();
	}

	protected ArrayList<String> tabComplete(ConsoleCommandSender sender, String[] args) {
		return new ArrayList<>();
	}

	protected ArrayList<String> tabComplete(BlockCommandSender sender, String[] args) {
		return new ArrayList<>();
	}

	@Override
	public boolean execute(@NotNull CommandSender sender, @NotNull String label, String[] args) {
		MessageManager manager = plugin.getMessageManager();

		if (sender instanceof Player player) {
			if (!isPlayerUsable) {
				player.sendMessage(manager.getPlayerMessage(Message.NOT_PLAYER_COMMAND, player));
			} else if (!execute(player, args)) {
				player.sendMessage(manager.getPlayerMessage(Message.INVALID_COMMAND_USAGE, player));
			}
		} else if (sender instanceof ConsoleCommandSender console) {
			if (!isConsoleUsable) {
				LogUtil.log(manager.getMessage(Message.NOT_CONSOLE_COMMAND));
			} else if (!execute(console, args)) {
				LogUtil.log(manager.getMessage(Message.INVALID_COMMAND_USAGE));
			}
		} else if (sender instanceof BlockCommandSender commandBlock) {
			if (!isCommandBlockUsable) {
				commandBlock.sendMessage(manager.getMessage(Message.NOT_COMMAND_BLOCK_COMMAND));
			} else if (!execute(commandBlock, args)) {
				commandBlock.sendMessage(manager.getMessage(Message.INVALID_COMMAND_USAGE));
			}
		}

		return true;
	}

	@NotNull
	@Override
	public ArrayList<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String @NotNull [] args) {
		ArrayList<String> results = null;

		if (sender instanceof Player player && isPlayerUsable) {
			results = tabComplete(player, args);
		} else if (sender instanceof ConsoleCommandSender console && isConsoleUsable) {
			results = tabComplete(console, args);
		} else if (sender instanceof BlockCommandSender commandBlock && isCommandBlockUsable) {
			results = tabComplete(commandBlock, args);
		}

		return results == null ? new ArrayList<>() : results;
	}
}
