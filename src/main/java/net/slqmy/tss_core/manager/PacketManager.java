package net.slqmy.tss_core.manager;

import io.netty.channel.*;
import net.minecraft.network.Connection;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.slqmy.tss_core.util.DebugUtil;
import net.slqmy.tss_core.util.ReflectUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PacketManager {

	private final ChannelDuplexHandler handler;

	public PacketManager() {
		handler = new ChannelDuplexHandler() {
			@Override
			public void read(ChannelHandlerContext context) throws Exception {
				super.read(context);
			}

			@Override
			public void write(ChannelHandlerContext context, Object message, ChannelPromise promise) throws Exception {
				super.write(context, message, promise);
			}
		};
	}

	public void injectPacketListener(@NotNull Player player) {
		Connection connection = getPlayerConnection(player);
		ChannelPipeline pipeline = connection.channel.pipeline();

		pipeline.addBefore(
						"packet_handler",
						player.getName(),
						handler
		);
	}

	public void ejectPacketListener(@NotNull Player player) {
		Connection connection = getPlayerConnection(player);
		ChannelPipeline pipeline = connection.channel.pipeline();

		Channel channel = connection.channel;
		channel.eventLoop().submit(() -> {
			pipeline.remove(player.getName());
			return null;
		});
	}

	private Connection getPlayerConnection(@NotNull Player player) {
		Connection connection = null;

		try {
			Method getHandle = ReflectUtil.getAccessibleMethod(player, "getHandle");
			assert getHandle != null;

			Object serverPlayer = getHandle.invoke(player);

			ServerGamePacketListenerImpl serverPlayerConnection = (ServerGamePacketListenerImpl) ReflectUtil.getFieldValue(serverPlayer, "c");
			assert serverPlayerConnection != null;

			connection = (Connection) ReflectUtil.getFieldValue(
							serverPlayerConnection,
							"h"
			);
		} catch (IllegalAccessException | InvocationTargetException exception) {
			DebugUtil.handleException("Reflection Failure: failed to get field 'connection' of player object of player " + player + "!", exception);
		}

		return connection;
	}
}
