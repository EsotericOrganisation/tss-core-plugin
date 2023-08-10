package net.slqmy.tss_core.manager;

import com.google.common.cache.Cache;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.slqmy.tss_core.TSSCorePlugin;
import net.slqmy.tss_core.data.Message;
import net.slqmy.tss_core.data.type.player.Language;
import net.slqmy.tss_core.data.type.player.PlayerProfile;
import net.slqmy.tss_core.util.CacheUtil;
import net.slqmy.tss_core.util.MessageUtil;
import net.slqmy.tss_core.util.type.Triplet;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ConcurrentMap;

public class MessageManager {

	private final TSSCorePlugin plugin;

	private final Language defualtLanguage;

	private final HashMap<Language, Triplet<YamlConfiguration, Cache<Message, String>, Cache<Message, TextComponent>>> languageData = new HashMap<>();

	public MessageManager(TSSCorePlugin plugin) {
		this.plugin = plugin;

		for (Language language : Language.values()) {
			File languageFile = plugin.getFileManager().initiateYamlFile("language/" + language.toString().toLowerCase());
			YamlConfiguration languageConfig = YamlConfiguration.loadConfiguration(languageFile);

			languageData.put(
							language,
							new Triplet<>(
											languageConfig,
											CacheUtil.getNewCache(),
											CacheUtil.getNewCache()
							)
			);
		}

		String defaultLanguageString = plugin.getConfig().getString("default-language");
		assert defaultLanguageString != null;

		defualtLanguage = Language.valueOf(defaultLanguageString.toUpperCase().replace('-', '_'));
	}

	private String getRawMessage(Message messageKey, Triplet<YamlConfiguration, Cache<Message, String>, Cache<Message, TextComponent>> data) {
		if (data == null) {
			data = languageData.get(defualtLanguage);
		}

		ConcurrentMap<Message, String> messageMap = data.getSecond().asMap();
		String message = messageMap.get(messageKey);
		if (message == null) {
			message = data.getFirst().getString(messageKey.getKey());
		}

		messageMap.put(messageKey, message);
		return message;
	}

	public TextComponent getMessage(Message messageKey, Language language, TextComponent @NotNull ... placeholderValues) {
		Triplet<YamlConfiguration, Cache<Message, String>, Cache<Message, TextComponent>> data = languageData.get(language);
		if (placeholderValues.length == 0) {
			ConcurrentMap<Message, TextComponent> messageMap = data.getThird().asMap();
			TextComponent message = messageMap.get(messageKey);

			if (message == null) {
				String rawMessage = getRawMessage(messageKey, data);
				message = (TextComponent) MiniMessage.miniMessage().deserialize(rawMessage);
			}

			return message;
		}

		String rawMessage = getRawMessage(messageKey, data);

		TagResolver[] tagResolvers = new TagResolver[placeholderValues.length];
		for (int i = 0; i < placeholderValues.length; i++) {
			@Subst("0") String componentText = String.valueOf(i);
			tagResolvers[i] = Placeholder.component(componentText, placeholderValues[i]);
		}

		return (TextComponent) MiniMessage.miniMessage().deserialize(rawMessage, tagResolvers);
	}

	private TextComponent getPlayerMessage(Message messageKey, Player player, TextComponent... placeholderValues) {
		PlayerProfile profile = plugin.getPlayerManager().getProfile(player);

		Language playerLanguage = profile.getPlayerPreferences().getLanguage();
		if (playerLanguage == null) {
			try {
				playerLanguage = Language.valueOf(player.locale().toLanguageTag().toUpperCase());
			} catch (IllegalArgumentException exception) {
				playerLanguage = defualtLanguage;
			}
		}

		return getMessage(messageKey, playerLanguage, placeholderValues);
	}

	public TextComponent getPlayerMessage(Message messageKey, Player player, Object... placeholderValues) {
		return getPlayerMessage(messageKey, player, asTextComponentArray(placeholderValues));
	}

	public TextComponent getPlayerMessage(Message messageKey, Player player) {
		return getPlayerMessage(messageKey, player, new TextComponent[]{});
	}

	public void sendMessage(@NotNull Player player, Message messageKey, TextComponent... placeholderValues) {
		player.sendMessage(getPlayerMessage(messageKey, player, placeholderValues));
	}

	public void sendMessage(@NotNull Player player, Message messageKey, Object... placeholderValues) {
		player.sendMessage(getPlayerMessage(messageKey, player, placeholderValues));
	}

	private TextComponent @NotNull [] asTextComponentArray(Object... values) {
		return Arrays.stream(values).map(MessageUtil::format).toArray(TextComponent[]::new);
	}
}
