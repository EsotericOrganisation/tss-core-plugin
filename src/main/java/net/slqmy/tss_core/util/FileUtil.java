package net.slqmy.tss_core.util;

import net.slqmy.tss_core.TSSCorePlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class FileUtil {
	public static void initiateFile(String path, @NotNull TSSCorePlugin plugin) {
		File dataFolder = plugin.getDataFolder();
		File file = new File(dataFolder, path);

		if (!file.exists()) {
			plugin.saveResource(path, false);
		}
	}
}
