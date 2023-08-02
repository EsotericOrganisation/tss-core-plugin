package net.slqmy.tss_core.manager;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.slqmy.tss_core.TSSCorePlugin;
import net.slqmy.tss_core.data.Message;
import net.slqmy.tss_core.data.type.Lang;
import net.slqmy.tss_core.type.PlayerProfile;
import net.slqmy.tss_core.util.DebugUtil;
import net.slqmy.tss_core.util.FileUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class MessageManager {

	private final static Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\d+}");

	private final TSSCorePlugin plugin;

	private final HashMap<Lang, YamlConfiguration> langConfigs = new HashMap<>();

	private final HashMap<Lang, Cache<Message, TextComponent>> messageCaches = new HashMap<>();

	public MessageManager(@NotNull TSSCorePlugin plugin) {
		this.plugin = plugin;

		for (Lang lang : Lang.values()) {
			File langFile = FileUtil.initiateFile("lang/" + lang.getCode() + ".yml", plugin);
			YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(langFile);

			langConfigs.put(lang, langConfig);

			messageCaches.put(
							lang,
							CacheBuilder.newBuilder()
											.expireAfterWrite(2, TimeUnit.MINUTES)
											.build()
			);
		}
	}

	public TextComponent getMessage(Message messageKey, Lang lang, Component ... placeHolderValues) {
		final TextComponent cachedMessage = messageCaches.get(lang).asMap().get(messageKey);

		if (cachedMessage != null) {
			return cachedMessage;
		}

		String rawMessage = getRawMessage(messageKey, lang);
		String[] splitMessage = PLACEHOLDER_PATTERN.split(rawMessage, placeHolderValues.length + 1);

		TextComponent finalMessage = LegacyComponentSerializer.legacyAmpersand().deserialize(splitMessage[0]);

		for (int i = 0; i < placeHolderValues.length; i++) {
			finalMessage = finalMessage.append(placeHolderValues[i]).append(LegacyComponentSerializer.legacyAmpersand().deserialize(splitMessage[i + 1]));
		}

		return finalMessage;
	}

	public TextComponent getMessage(Message messageKey, Component ... placeHolderValues) {
		return getMessage(messageKey, Lang.DEFAULT_LANG, placeHolderValues);
	}

	public TextComponent getMessage(Message messageKey, String ... placeHolderValues) {
		return getMessage(
						messageKey,
						Lang.DEFAULT_LANG,
						Arrays.stream(placeHolderValues).map((String placeHolderValue) -> LegacyComponentSerializer.legacyAmpersand().deserialize(placeHolderValue)).toArray(TextComponent[]::new)
		);
	}

	public TextComponent getMessage(Message messageKey) {
		return getMessage(
						messageKey,
						Lang.DEFAULT_LANG
		);
	}

	public TextComponent getPlayerMessage(Message messageKey, Player player, Component... placeHolderValues) {
		PlayerProfile profile = plugin.getPlayerManager().getProfile(player);

		Lang lang = profile == null ||
						profile.getPreferences() == null ||
						profile.getPreferences().getLang() == null
						? Lang.DEFAULT_LANG
						: profile.getPreferences().getLang();

		return getMessage(messageKey, lang, placeHolderValues);
	}

	public TextComponent getPlayerMessage(Message messageKey, Player player, String... placeHolderValues) {
		return getPlayerMessage(
						messageKey,
						player,
						Arrays.stream(placeHolderValues).map((String placeHolderValue) -> LegacyComponentSerializer.legacyAmpersand().deserialize(placeHolderValue)).toArray(TextComponent[]::new)
		);
	}

	public TextComponent getPlayerMessage(Message messageKey, Player player) {
		return getPlayerMessage(messageKey, player, new Component[]{});
	}

	private String getRawMessage(@NotNull Message messageKey, Lang lang) {
		DebugUtil.log(langConfigs, langConfigs.get(lang), langConfigs.get(lang).getString(messageKey.getKey()), messageKey, messageKey.getKey());

		return langConfigs.get(lang).getString(messageKey.getKey());
	}

	private String getRawMessage(@NotNull Message messageKey) {
		return getRawMessage(messageKey, Lang.DEFAULT_LANG);
	}
}
