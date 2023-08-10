package net.slqmy.tss_core.data.type.player;

import org.jetbrains.annotations.NotNull;

public enum Language {
	EN_GB,
	EN_US;

	public static Language getLanguage(@NotNull String languageKey) {
		return Language.valueOf(languageKey.toUpperCase().replace('-', '_'));
	}

	public @NotNull String getLanguageKey() {
		return name().toLowerCase().replace('_', '-');
	}
}
