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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

	Object action = ReflectUtil.getFieldValue(packet, "c");

	Method getTypeMethod;
	Object actionType = null;

	getTypeMethod = ReflectUtil.getAccessibleMethod(action, "a");
	try {
		actionType = getTypeMethod.invoke(action);
	} catch (IllegalAccessException | InvocationTargetException exception) {
		exception.printStackTrace();
	}

	Class<?>[] classes = ServerboundInteractPacket.class.getDeclaredClasses();

	Class<?> actionTypeEnumClass = null;

	for (Class<?> classVariable : classes) {
		if (classVariable.getSimpleName().equals("ActionType") && classVariable.isEnum()) {
			actionTypeEnumClass = classVariable;
			break;
		}
	}

	Object[] enumConstants = actionTypeEnumClass.getEnumConstants();

	Object interactAtConstant = enumConstants[2];

	if (actionType == interactAtConstant) {
	  return;
	}

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
