package org.esoteric.tss.minecraft.plugins.core.managers;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.core.BlockPos;
import net.minecraft.locale.Language;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.ChatVisiblity;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.esoteric.tss.minecraft.plugins.core.TSSCorePlugin;
import org.esoteric.tss.minecraft.plugins.core.data.npc.NPC;
import org.esoteric.tss.minecraft.plugins.core.data.npc.NPCData;
import org.esoteric.tss.minecraft.plugins.core.data.player.Skin;
import org.esoteric.tss.minecraft.plugins.core.util.NMSUtil;
import org.esoteric.tss.minecraft.plugins.core.util.types.Pair;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

public class NPCManager {

    private final TSSCorePlugin plugin;

    private final HashMap<Integer, NPC> npcs = new HashMap<>();

    public NPCManager(@NotNull TSSCorePlugin plugin) {
        this.plugin = plugin;

        FileManager fileManager = plugin.getFileManager();

        File npcFile = fileManager.initiateJsonFile("npcs");
        NPCData[] npcDataFileData = fileManager.readJsonFile(npcFile, NPCData[].class);
        assert npcDataFileData != null;

        for (NPCData npc : npcDataFileData) {
            Pair<Integer, ServerPlayer> npcData = spawnNpc(npc);

            npcs.put(
                    npcData.getFirst(),
                    new NPC(npc, npcData.getSecond())
            );
        }
    }

    public HashMap<Integer, NPC> getNpcs() {
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
                npcProfile,
                new ClientInformation(Language.DEFAULT, 12, ChatVisiblity.FULL, false, 0, HumanoidArm.RIGHT, false, false)
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

        for (NPC npc : npcs.values()) {
            ServerPlayer nmsEntity = npc.getNmsEntity();

            if (!nmsEntity.getBukkitEntity().getWorld().equals(playerWorld)) {
                continue;
            }

            Skin npcSkin = npc.getSkin();

            nmsEntity.getGameProfile().getProperties().put("textures", new Property("texture", npcSkin.getValue(), npcSkin.getSignature()));

            connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, nmsEntity));
            connection.send(new ClientboundSetEntityDataPacket(nmsEntity.getId(), Collections.singletonList(SynchedEntityData.DataValue.create(
                    new EntityDataAccessor<>(17, EntityDataSerializers.BYTE), (byte) 125)
            )));
            connection.send(new ClientboundAddEntityPacket(nmsEntity, 0, new BlockPos((int) npc.getSimpleLocation().asBukkitLocation().getX(), (int) npc.getSimpleLocation().asBukkitLocation().getY(), (int) npc.getSimpleLocation().asBukkitLocation().getZ())));

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
