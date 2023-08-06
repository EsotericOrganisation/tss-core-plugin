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
		return (ServerPlayer) invokeGetHandle(player);
	}

	public static @Nullable Object invokeGetHandle(Object spigotObject) {
		Method getHandle = ReflectUtil.getAccessibleMethod(spigotObject, "getHandle");
		assert getHandle != null;

		try {
			return getHandle.invoke(spigotObject);
		} catch (IllegalAccessException | InvocationTargetException exception) {
			DebugUtil.handleException("An unexpected error occurred while invoking the 'getHandle' method of Spigot object '" + spigotObject + "'!", exception);
		}

		return null;
	}
}
