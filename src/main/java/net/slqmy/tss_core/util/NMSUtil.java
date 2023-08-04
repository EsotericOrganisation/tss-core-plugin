package net.slqmy.tss_core.util;

import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NMSUtil {

	private static final String NMS_CLASS_PREFIX = "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + ".";

	public static Class<?> getNMSClass(String nmsClassName) {
		return ReflectUtil.getClassByName(NMS_CLASS_PREFIX + nmsClassName);
	}

	public static @Nullable ServerPlayer getServerPlayer(Player player) {
		Method getHandle = ReflectUtil.getAccessibleMethod(player, "getHandle");
		assert getHandle != null;

		try {
			return (ServerPlayer) getHandle.invoke(player);
		} catch (IllegalAccessException | InvocationTargetException exception) {
			DebugUtil.handleException("An unexpected error occurred while invoking the 'getHandle' method of player object of player '" + player + "'!", exception);
		}

		return null;
	}
}
