package net.slqmy.tss_core.data.type;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SimpleLocation {

	private String worldName;

	private double x;
	private double y;
	private double z;

	private float yaw;
	private float pitch;

	public Location asBukkitLocation() {
		return new Location(
						Bukkit.getWorld(worldName),
						x,
						y,
						z,
						yaw,
						pitch
		);
	}
}
