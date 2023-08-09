package net.slqmy.tss_core.manager;

import com.google.common.cache.Cache;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.slqmy.tss_core.TSSCorePlugin;
import net.slqmy.tss_core.data.Message;
import net.slqmy.tss_core.data.type.player.Language;
import net.slqmy.tss_core.data.type.player.PlayerProfile;
import net.slqmy.tss_core.util.CacheUtil;
import net.slqmy.tss_core.util.FileUtil;
import net.slqmy.tss_core.util.type.Triplet;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

public class MessageManager {

	private final TSSCorePlugin plugin;

	private final HashMap<
					Language,
					Triplet<
									YamlConfiguration,
									Cache<Message, String>,
									Cache<Message, TextComponent>
									>
					> languageData = new HashMap<>();


	public MessageManager(@NotNull TSSCorePlugin plugin) {
		this.plugin = plugin;

		for (Language language : Language.values()) {
			File langFile = FileUtil.initiateYamlFile("lang/" + language.getCode(), true, plugin);
			YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(langFile);

			languageData.put(
							language,
							new Triplet<>(
											langConfig,
											CacheUtil.getNewCache(),
											CacheUtil.getNewCache()
							)
			);
		}
	}

	private String getRawMessage(Language language, Message messageKey) {
		Triplet<YamlConfiguration, Cache<Message, String>, Cache<Message, TextComponent>> triplet = languageData.get(language);

		if (triplet == null) {
			triplet = languageData.get(Language.DEFAULT_LANGUAGE);
		}

		Cache<Message, String> cache = triplet.getSecond();
		String message = cache.asMap().get(messageKey);

		if (message == null) {
			message = (String) triplet.getFirst().get(messageKey.getKey());
		}

		CacheUtil.refreshExpiryTimer(cache, messageKey, message);
		return message;
	}

	public TextComponent getMessage(Message messageKey, Language language, TextComponent @NotNull ... placeholderValues) {
		if (placeholderValues.length == 0) {
			Triplet<YamlConfiguration, Cache<Message, String>, Cache<Message, TextComponent>> triplet = languageData.get(language);

			if (triplet == null) {
				triplet = languageData.get(Language.DEFAULT_LANGUAGE);
			}

			Cache<Message, TextComponent> cache = triplet.getThird();

			TextComponent message = cache.asMap().get(messageKey);

			if (message == null) {
				message = (TextComponent) MiniMessage.miniMessage().deserialize((String) triplet.getFirst().get(messageKey.getKey()));
			}

			CacheUtil.refreshExpiryTimer(cache, messageKey, message);
			return message;
		}

		String rawMessage = getRawMessage(language, messageKey);

		TagResolver[] tagResolvers = new TagResolver[placeholderValues.length];
		for (int i = 0; i < placeholderValues.length; i++) {
			tagResolvers[i] = Placeholder.component(String.valueOf(i), placeholderValues[i]);
		}

		return (TextComponent) MiniMessage.miniMessage().deserialize(rawMessage, tagResolvers);
	}

	public TextComponent getMessage(Message messageKey, String... placeholderValues) {
		return getMessage(messageKey, Language.DEFAULT_LANGUAGE, toTextComponentArray(placeholderValues));
	}

	public TextComponent getMessage(Message messageKey) {
		return getMessage(messageKey, Language.DEFAULT_LANGUAGE);
	}

	public TextComponent getPlayerMessage(Message messageKey, Player player, TextComponent @NotNull ... placeholderValues) {
		PlayerProfile profile = plugin.getPlayerManager().getProfile(player);

		Language language = profile == null ||
						profile.getPlayerPreferences() == null ||
						profile.getPlayerPreferences().getLanguage() == null
						? Language.DEFAULT_LANGUAGE
						: profile.getPlayerPreferences().getLanguage();

		return getMessage(messageKey, language, placeholderValues);
	}

	public TextComponent getPlayerMessage(Message messageKey, Player player, String @NotNull ... placeholderValues) {
		return getPlayerMessage(messageKey, player, toTextComponentArray(placeholderValues));
	}

	public TextComponent getPlayerMessage(Message messageKey, Player player) {
		return getPlayerMessage(messageKey, player, new TextComponent[]{});
	}

	private TextComponent @NotNull [] toTextComponentArray(String[] strings) {
		return Arrays.stream(strings).map(Component::text).toArray(TextComponent[]::new);
	}
}
