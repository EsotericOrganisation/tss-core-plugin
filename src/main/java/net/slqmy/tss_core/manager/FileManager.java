package net.slqmy.tss_core.manager;

import com.google.gson.Gson;
import net.slqmy.tss_core.util.DebugUtil;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;

public class FileManager {

  private final JavaPlugin plugin;

  public FileManager(JavaPlugin plugin) {
	this.plugin = plugin;
  }

  public @NotNull File initiateFile(String path, boolean replace) {
	File dataFolder = plugin.getDataFolder();

	File file = new File(dataFolder, path);

	if (replace || !file.exists()) {
	  plugin.saveResource(path, replace);
	}

	return file;
  }

  public @NotNull File initiateYamlFile(String path, boolean replace) {
	return initiateFile(path + ".yml", replace);
  }

  public @NotNull File initiateYamlFile(String path) {
	return initiateFile(path + ".yml", false);
  }

  public @NotNull File initiateJsonFile(String path, boolean replace) {
	return initiateFile(path + ".json", replace);
  }

  public @NotNull File initiateJsonFile(String path) {
	return initiateFile(path + ".json", false);
  }

  public <T> void saveJsonFile(String path, T data, Class<T> targetClass, boolean replace) {
	File jsonFile = initiateFile(path + ".json", replace);

	try (Writer writer = new FileWriter(jsonFile, false)) {
	  new Gson().toJson(data, writer);
	  writer.flush();
	} catch (IOException exception) {
	  DebugUtil.handleException("An unexpected exception occurred while saving JSON file " + jsonFile + " of type " + targetClass + "!", exception);
	  DebugUtil.log("Data: " + data);
	}
  }

  public <T> void saveJsonFile(String path, T data, Class<T> targetClass) {
	saveJsonFile(path, data, targetClass, false);
  }

  public <T> @Nullable T readJsonFile(File jsonFile, Class<T> targetClass) {
	try {
	  Reader reader = new FileReader(jsonFile);
	  return new Gson().fromJson(reader, targetClass);
	} catch (FileNotFoundException exception) {
	  DebugUtil.handleException("An unexpected exception occurred while reading JSON file " + jsonFile + " of target class " + targetClass + "!", exception);
	}

	return null;
  }

  public <T> @Nullable T readJsonFile(String path, Class<T> targetClass) {
	return readJsonFile(initiateJsonFile(path), targetClass);
  }
}
