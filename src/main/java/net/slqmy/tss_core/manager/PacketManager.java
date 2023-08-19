package net.slqmy.tss_core.manager;

import io.netty.channel.*;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.world.InteractionHand;
import net.slqmy.tss_core.TSSCorePlugin;
import net.slqmy.tss_core.datatype.npc.NPC;
import net.slqmy.tss_core.event.custom_event.NPCClickEvent;
import net.slqmy.tss_core.util.NMSUtil;
import net.slqmy.tss_core.util.ReflectUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PacketManager {

  private final TSSCorePlugin plugin;

  public PacketManager(TSSCorePlugin plugin) {
	this.plugin = plugin;
  }

  public void injectPlayer(@NotNull Player player) {
	ChannelDuplexHandler handler = new ChannelDuplexHandler() {
	  @Override
	  public void channelRead(@NotNull ChannelHandlerContext context, @NotNull Object packet) throws Exception {
		if (packet instanceof ServerboundInteractPacket) {
		  handleServerboundInteractPacket((ServerboundInteractPacket) packet, player);
		}

		super.channelRead(context, packet);
	  }

	  @Override
	  public void write(ChannelHandlerContext context, Object packet, ChannelPromise promise) throws Exception {
		super.write(context, packet, promise);
	  }
	};

	ChannelPipeline pipeline = NMSUtil.getServerPlayer(player).connection.connection.channel.pipeline();

	pipeline.addBefore(
			"packet_handler",
			player.getName(),
			handler
	);
  }

  public void ejectPlayer(@NotNull Player player) {
	Connection connection = NMSUtil.getServerPlayer(player).connection.connection;
	ChannelPipeline pipeline = connection.channel.pipeline();

	Channel channel = connection.channel;
	channel.eventLoop().submit(() -> {
	  pipeline.remove(player.getName());
	  return null;
	});
  }

  private void handleServerboundInteractPacket(@NotNull ServerboundInteractPacket packet, Player player) {
	Integer entityID = packet.getEntityId();
	ServerboundInteractPacket.ActionType actionType = packet.getActionType();

	if (actionType == ServerboundInteractPacket.ActionType.INTERACT_AT) {
	  return;
	}

	Object action = ReflectUtil.getFieldValue(packet, "b");
	assert action != null;

	InteractionHand hand = (InteractionHand) ReflectUtil.getFieldValue(action, "a", false);

	if (hand == InteractionHand.OFF_HAND) {
	  return;
	}

	NPC npc = plugin.getNpcManager().getNpcs().get(entityID);

	if (npc != null) {
	  Bukkit.getPluginManager().callEvent(new NPCClickEvent(npc, player));
	}
  }
}
