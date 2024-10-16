package org.esoteric.tss.minecraft.plugins.core.managers;

import com.google.common.cache.Cache;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.esoteric.tss.minecraft.plugins.core.TSSCorePlugin;
import org.esoteric.tss.minecraft.plugins.core.data.player.Language;
import org.esoteric.tss.minecraft.plugins.core.data.player.Message;
import org.esoteric.tss.minecraft.plugins.core.data.player.PlayerProfile;
import org.esoteric.tss.minecraft.plugins.core.util.CacheUtil;
import org.esoteric.tss.minecraft.plugins.core.util.DebugUtil;
import org.esoteric.tss.minecraft.plugins.core.util.MessageUtil;
import org.esoteric.tss.minecraft.plugins.core.util.types.Triplet;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ConcurrentMap;

public class MessageManager {

    private final TSSCorePlugin plugin;

    private final Language defaultLanguage;

    private final HashMap<Language, Triplet<YamlConfiguration, Cache<Message, String>, Cache<Message, TextComponent>>> languageData = new HashMap<>();

    public MessageManager(TSSCorePlugin plugin) {
        this.plugin = plugin;

        for (Language language : Language.values()) {
            File languageFile = plugin.getFileManager().initiateYamlFile("language/" + language.getLanguageKey(), true);
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

        defaultLanguage = Language.valueOf(defaultLanguageString.toUpperCase().replace('-', '_'));
    }

    private String getRawMessage(Message messageKey, Triplet<YamlConfiguration, Cache<Message, String>, Cache<Message, TextComponent>> data) {
        if (data == null) {
            data = languageData.get(defaultLanguage);
        }

        ConcurrentMap<Message, String> messageMap = data.getSecond().asMap();
        String message = messageMap.get(messageKey);
        if (message == null) {
            DebugUtil.log(messageKey.getKey());
            DebugUtil.log(data.getFirst().getString(messageKey.getKey()));

            String key = messageKey.getKey();
            message = data.getFirst().getString(key);
            if (message == null) {
                data = languageData.get(defaultLanguage);

                messageMap = data.getSecond().asMap();
                message = messageMap.get(messageKey);

                if (message == null) {
                    message = data.getFirst().getString(key);
                }
            }
        }

        messageMap.put(messageKey, message);
        return message;
    }

    public TextComponent getMessage(Message messageKey, Language language, TextComponent @NotNull ... placeholderValues) {
        Triplet<YamlConfiguration, Cache<Message, String>, Cache<Message, TextComponent>> data = languageData.get(language);

        if (data == null) {
            data = languageData.get(defaultLanguage);
        }

        if (placeholderValues.length == 0) {
            ConcurrentMap<Message, TextComponent> messageMap = data.getThird().asMap();
            TextComponent message = messageMap.get(messageKey);

            if (message == null) {
                String rawMessage = getRawMessage(messageKey, data);
                message = (TextComponent) MiniMessage.miniMessage().deserialize(rawMessage);
            }

            messageMap.put(messageKey, message);
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

        Language playerLanguage;

        try {
            playerLanguage = profile.getPlayerPreferences().getLanguage();

            if (playerLanguage == null) {
                throw new NullPointerException();
            }
        } catch (NullPointerException nullPointerException) {
            try {
                playerLanguage = Language.getLanguage(player.locale().toLanguageTag());
            } catch (IllegalArgumentException illegalArgumentException) {
                playerLanguage = defaultLanguage;
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
