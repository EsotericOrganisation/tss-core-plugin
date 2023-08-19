package net.slqmy.tss_core.datatype;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class Rank {

  private int weight;

  private String name;
  private String displayName;
  private String namePrefix;
  private String nameSuffix;

  private List<Permission> permissions;

  private FireworkType fireworkType;

  private int initialSurvivalClaimChunks;

  public Rank() {

  }

  public int getWeight() {
	return weight;
  }

  public String getName() {
	return name;
  }

  public @NotNull TextComponent getDisplayName() {
	return (TextComponent) MiniMessage.miniMessage().deserialize(displayName);
  }

  public @NotNull TextComponent getNamePrefix() {
	if (namePrefix == null || "".equals(namePrefix)) {
	  return Component.empty();
	}

	return (TextComponent) MiniMessage.miniMessage().deserialize(namePrefix).append(Component.space());
  }

  public @NotNull TextComponent getNameSuffix() {
	if (nameSuffix == null || "".equals(nameSuffix)) {
	  return Component.empty();
	}

	return Component.space().append(MiniMessage.miniMessage().deserialize(nameSuffix));
  }

  public List<Permission> getPermissions() {
	if (permissions != null) {
	  return permissions;
	}

	return new ArrayList<>();
  }

  public FireworkType getFireworkType() {
	return fireworkType;
  }

  public int getInitialSurvivalClaimChunks() {
	return initialSurvivalClaimChunks;
  }
}
