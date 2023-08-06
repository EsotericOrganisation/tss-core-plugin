package net.slqmy.tss_core.manager;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.phys.Vec3;
import net.slqmy.tss_core.TSSCorePlugin;
import net.slqmy.tss_core.data.type.NPC;
import net.slqmy.tss_core.data.type.NPCData;
import net.slqmy.tss_core.data.type.Skin;
import net.slqmy.tss_core.util.DebugUtil;
import net.slqmy.tss_core.util.FileUtil;
import net.slqmy.tss_core.util.NMSUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

public class NPCManager {

	private final TSSCorePlugin plugin;

	private final HashMap<Integer, NPC> npcs = new HashMap<>();

	public NPCManager(TSSCorePlugin plugin) {
		this.plugin = plugin;

		File npcFile = FileUtil.initiateJsonFile("npcs", plugin);
		NPCData npcData = FileUtil.readJsonFile(npcFile, NPCData.class);
		assert npcData != null;

		for (NPC npc : npcData.getNpcs()) {
			Skin npcSkin = npc.getSkin();

			int id = spawnNpc(
							npc.getSimpleLocation().asLocation(),
							npcSkin.getValue(),
							npcSkin.getValue()
			);

			npcs.put(id, npc);
		}
	}

	public int spawnNpc(@NotNull Location location, String skinValue, String skinSignature) {
		GameProfile npcProfile = new GameProfile(
						UUID.randomUUID(),
						"NPC"
		);

		npcProfile.getProperties().put("textures", new Property("texture", skinValue, skinSignature));

		DebugUtil.log(skinValue, skinSignature);

		MinecraftServer server = MinecraftServer.getServer();
		ServerPlayer npc = new ServerPlayer(
						server,
						(ServerLevel) NMSUtil.invokeGetHandle(location.getWorld()),
						npcProfile
		);

		npc.setPos(
						location.getX(),
						location.getY(),
						location.getZ()
		);

		SynchedEntityData data = npc.getEntityData();
		byte bitmask = (byte) 125;

		data.set(
						new EntityDataAccessor<>(17, EntityDataSerializers.BYTE),
						bitmask
		);

		plugin.getNpcs().put(npc.getId(), npc);
		return npc.getId();
	}

	public void addPlayer(Player player) {
		ServerPlayer serverPlayer = NMSUtil.getServerPlayer(player);
		assert serverPlayer != null;

		for (ServerPlayer npc : plugin.getNpcs().values()) {
			ServerGamePacketListenerImpl connection = serverPlayer.connection;

			// npc.getGameProfile().getProperties().put("textures", new Property("texture"));

			connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, npc));
			connection.send(new ClientboundSetEntityDataPacket(npc.getId(), Collections.singletonList(SynchedEntityData.DataValue.create(
							new EntityDataAccessor<>(17, EntityDataSerializers.BYTE), (byte) 125)
			)));
			connection.send(new ClientboundAddPlayerPacket(npc));

			new BukkitRunnable() {

				private final Vec3 location = npc.getEyePosition();

				@Override
				public void run() {
					Location playerLocation = player.getEyeLocation();

					double playerX = playerLocation.x();
					double playerZ = playerLocation.z();

					double npcX = location.x();
					double npcZ = location.z();

					double horizontalDistance = Math.sqrt(Math.pow(playerX - npcX, 2) + Math.pow(playerZ - npcZ, 2));

					double yaw = 90D - Math.toDegrees(Math.atan2(playerZ - npcZ, npcX - playerX));
					double pitch = -Math.toDegrees(Math.atan2(playerLocation.getY() - location.y(), horizontalDistance));

					connection.send(new ClientboundRotateHeadPacket(
									npc,
									(byte) (yaw * 256D / 360D)
					));

					connection.send(
									new ClientboundMoveEntityPacket.Rot(
													npc.getBukkitEntity().getEntityId(),
													(byte) (yaw * 256D / 360D),
													(byte) (pitch * 256D / 360D),
													true
									)
					);
				}
			}.runTaskTimer(plugin, 0, 1);
		}
	}
}
