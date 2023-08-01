package net.slqmy.tss_core.manager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.slqmy.tss_core.TSSCorePlugin;
import net.slqmy.tss_core.data.Message;
import net.slqmy.tss_core.data.type.Lang;
import net.slqmy.tss_core.type.PlayerProfile;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;

public class MessageManager {

	private final TSSCorePlugin plugin;

	private final HashMap<Lang, YamlConfiguration> langConfigs = new HashMap<>();

	public MessageManager(@NotNull TSSCorePlugin plugin) {
		this.plugin = plugin;

		for (Lang lang : Lang.values()) {
			File langFile = new File(plugin.getDataFolder() + "/lang/" + lang.getCode() + ".yml");
			YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(langFile);

			langConfigs.put(lang, langConfig);
		}
	}

	public Component getPlayerMessage(Message messageKey, Player player) {
		PlayerProfile profile = plugin.getPlayerManager().getProfile(player);

		Lang lang;

		if (profile == null || profile.getPreferences() == null || profile.getPreferences().getLang() == null) {
			lang = Lang.DEFAULT_LANG;
		} else {
			lang = profile.getPreferences().getLang();
		}

		return LegacyComponentSerializer.legacyAmpersand().deserialize(
						getRawMessage(messageKey, lang)
		);
	}

	public String getRawMessage(@NotNull Message messageKey, Lang lang) {
		return langConfigs.get(lang).getString(messageKey.getKey());
	}

	public String getRawMessage(@NotNull Message messageKey) {
		return getRawMessage(messageKey, Lang.DEFAULT_LANG);
	}
}
