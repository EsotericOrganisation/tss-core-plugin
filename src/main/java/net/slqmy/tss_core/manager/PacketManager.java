package net.slqmy.tss_core.manager;

import io.netty.channel.*;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.slqmy.tss_core.util.NMSUtil;
import net.slqmy.tss_core.util.ReflectUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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

	public void injectPlayer(@NotNull Player player) {
		Connection connection = getPlayerConnection(player);
		ChannelPipeline pipeline = connection.channel.pipeline();

		pipeline.addBefore(
						"packet_handler",
						player.getName(),
						handler
		);
	}

	public void ejectPlayer(@NotNull Player player) {
		Connection connection = getPlayerConnection(player);
		ChannelPipeline pipeline = connection.channel.pipeline();

		Channel channel = connection.channel;
		channel.eventLoop().submit(() -> {
			pipeline.remove(player.getName());
			return null;
		});
	}

	private Connection getPlayerConnection(@NotNull Player player) {
		ServerPlayer serverPlayer = NMSUtil.getServerPlayer(player);
		assert serverPlayer != null;

		ServerGamePacketListenerImpl serverPlayerConnection = (ServerGamePacketListenerImpl) ReflectUtil.getFieldValue(serverPlayer, "c");
		assert serverPlayerConnection != null;

		return (Connection) ReflectUtil.getFieldValue(
						serverPlayerConnection,
						"h"
		);
	}
}
