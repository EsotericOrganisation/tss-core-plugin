package net.slqmy.tss_core.datatype.player;

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
