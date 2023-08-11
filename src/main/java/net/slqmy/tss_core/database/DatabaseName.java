package net.slqmy.tss_core.database;

public enum DatabaseName {
  PLAYERS;

  private final String name;

  DatabaseName() {
	name = name().toLowerCase();
  }

  public String getName() {
	return name;
  }
}
