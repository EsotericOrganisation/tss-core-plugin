package net.slqmy.tss_core.util;

import com.google.gson.Gson;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;

public class FileUtil {
	public static @NotNull File initiateFile(String path, boolean replace, @NotNull JavaPlugin plugin) {
		File dataFolder = plugin.getDataFolder();

		File file = new File(dataFolder, path);

		if (replace || !file.exists()) {
			plugin.saveResource(path, replace);
		}

		return file;
	}

	public static @NotNull File initiateYamlFile(String path, boolean replace, @NotNull JavaPlugin plugin) {
		return initiateFile(path + ".yml", replace, plugin);
	}

	public static @NotNull File initiateYamlFile(String path, @NotNull JavaPlugin plugin) {
		return initiateFile(path + ".yml", false, plugin);
	}

	public static @NotNull File initiateJsonFile(String path, boolean replace, @NotNull JavaPlugin plugin) {
		return initiateFile(path + ".json", replace, plugin);
	}

	public static @NotNull File initiateJsonFile(String path, @NotNull JavaPlugin plugin) {
		return initiateFile(path + ".json", false, plugin);
	}

	public static <T> void saveJsonFile(String path, T data, Class<T> targetClass, boolean replace, @NotNull JavaPlugin plugin) {
		File jsonFile = initiateFile(path + ".json", replace, plugin);

		try (Writer writer = new FileWriter(jsonFile, false)) {
			new Gson().toJson(data, writer);
			writer.flush();
		} catch (IOException exception) {
			DebugUtil.handleException("An unexpected exception occurred while saving JSON file " + jsonFile + " of type " + targetClass + "!", exception);
			DebugUtil.log("Data: " + data);
		}
	}

	public static <T> void saveJsonFile(String path, T data, Class<T> targetClass, @NotNull JavaPlugin plugin) {
		saveJsonFile(path, data, targetClass, false, plugin);
	}

	public static <T> @Nullable T readJsonFile(File jsonFile, Class<T> targetClass) {
		try {
			Reader reader = new FileReader(jsonFile);
			return new Gson().fromJson(reader, targetClass);
		} catch (FileNotFoundException exception) {
			DebugUtil.handleException("An unexpected exception occurred while reading JSON file " + jsonFile + " of target class " + targetClass + "!", exception);
		}

		return null;
	}

	public static <T> @Nullable T readJsonFile(String path, Class<T> targetClass, JavaPlugin plugin) {
		return readJsonFile(initiateJsonFile(path, plugin), targetClass);
	}
}
