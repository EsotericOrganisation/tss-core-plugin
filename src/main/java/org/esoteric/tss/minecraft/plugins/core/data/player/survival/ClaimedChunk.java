package org.esoteric.tss.minecraft.plugins.core.data.player.survival;

import java.util.ArrayList;
import java.util.UUID;

public class ClaimedChunk {
    private int x;
    private int z;

    private ArrayList<UUID> trustedPlayers;

    public ClaimedChunk() {

    }

    public ClaimedChunk(int x, int z, ArrayList<UUID> trustedPlayers) {
        this.x = x;
        this.z = z;

        this.trustedPlayers = trustedPlayers;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public ArrayList<UUID> getTrustedPlayers() {
        return trustedPlayers;
    }

    public void setTrustedPlayers(ArrayList<UUID> trustedPlayers) {
        this.trustedPlayers = trustedPlayers;
    }
}
