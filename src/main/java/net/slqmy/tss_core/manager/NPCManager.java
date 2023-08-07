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
import net.slqmy.tss_core.data.type.Skin;
import net.slqmy.tss_core.data.type.npc.NPCData;
import net.slqmy.tss_core.type.NPCPlayer;
import net.slqmy.tss_core.util.FileUtil;
import net.slqmy.tss_core.util.NMSUtil;
import net.slqmy.tss_core.util.type.Pair;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

public class NPCManager {

	private final TSSCorePlugin plugin;

	private final HashMap<Integer, NPCPlayer> npcs = new HashMap<>();

	public NPCManager(TSSCorePlugin plugin) {
		this.plugin = plugin;

		File npcFile = FileUtil.initiateJsonFile("npcs", plugin);
		NPCData[] npcDataFileData = FileUtil.readJsonFile(npcFile, NPCData[].class);
		assert npcDataFileData != null;

		for (NPCData npc : npcDataFileData) {
			Pair<Integer, ServerPlayer> npcData = spawnNpc(npc);

			npcs.put(
							npcData.getFirst(),
							new NPCPlayer(npc, npcData.getSecond())
			);
		}
	}

	public HashMap<Integer, NPCPlayer> getNpcs() {
		return npcs;
	}

	private @NotNull Pair<Integer, ServerPlayer> spawnNpc(@NotNull NPCData npcData) {
		GameProfile npcProfile = new GameProfile(
						UUID.randomUUID(),
						"NPC"
		);

		Skin skin = npcData.getSkin();
		npcProfile.getProperties().put("textures", new Property("textures", skin.getValue(), skin.getSignature()));

		MinecraftServer server = MinecraftServer.getServer();
		Location location = npcData.getSimpleLocation().asBukkitLocation();
		ServerPlayer npcPlayer = new ServerPlayer(
						server,
						(ServerLevel) NMSUtil.invokeGetHandle(location.getWorld()),
						npcProfile
		);

		npcPlayer.setPos(
						location.getX(),
						location.getY(),
						location.getZ()
		);

		SynchedEntityData data = npcPlayer.getEntityData();

		data.set(
						new EntityDataAccessor<>(17, EntityDataSerializers.BYTE),
						(byte) 125
		);

		return new Pair<>(npcPlayer.getId(), npcPlayer);
	}

	public void updateNpcs(@NotNull Player player) {
		World playerWorld = player.getWorld();

		ServerPlayer serverPlayer = NMSUtil.getServerPlayer(player);
		assert serverPlayer != null;

		ServerGamePacketListenerImpl connection = serverPlayer.connection;

		for (NPCPlayer npc : npcs.values()) {
			ServerPlayer nmsEntity = npc.getNmsEntity();

			if (!nmsEntity.getBukkitEntity().getWorld().equals(playerWorld)) {
				continue;
			}

			NPCData npcData = npc.getNpc();
			Skin npcSkin = npcData.getSkin();

			nmsEntity.getGameProfile().getProperties().put("textures", new Property("texture", npcSkin.getValue(), npcSkin.getSignature()));

			connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, nmsEntity));
			connection.send(new ClientboundSetEntityDataPacket(nmsEntity.getId(), Collections.singletonList(SynchedEntityData.DataValue.create(
							new EntityDataAccessor<>(17, EntityDataSerializers.BYTE), (byte) 125)
			)));
			connection.send(new ClientboundAddPlayerPacket(nmsEntity));

			new BukkitRunnable() {

				private final Vec3 location = nmsEntity.getEyePosition();

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
									nmsEntity,
									(byte) (yaw * 256D / 360D)
					));

					connection.send(
									new ClientboundMoveEntityPacket.Rot(
													nmsEntity.getBukkitEntity().getEntityId(),
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
