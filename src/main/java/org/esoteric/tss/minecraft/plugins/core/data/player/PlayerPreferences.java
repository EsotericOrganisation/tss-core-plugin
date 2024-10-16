package org.esoteric.tss.minecraft.plugins.core.data.player;

public class PlayerPreferences {

    private Language language;
    private boolean lobbyFireworkEnabled = true;

    public PlayerPreferences() {

    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public boolean isLobbyFireworkEnabled() {
        return lobbyFireworkEnabled;
    }

    public void setLobbyFireworkEnabled(boolean lobbyFireworkEnabled) {
        this.lobbyFireworkEnabled = lobbyFireworkEnabled;
    }
}
