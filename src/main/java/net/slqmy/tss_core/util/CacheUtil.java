package net.slqmy.tss_core.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class CacheUtil {

  private static final long DEFAULT_CACHE_DURATION = 5;
  private static final TimeUnit DEFAULT_CACHE_TIME_UNIT = TimeUnit.MINUTES;

  public static <K, V> @NotNull Cache<K, V> getNewCache(long duration, @NotNull TimeUnit timeUnit) {
	return CacheBuilder.newBuilder()
			.expireAfterWrite(duration, timeUnit)
			.build();
  }

  public static <K, V> @NotNull Cache<K, V> getNewCache(@NotNull Duration duration) {
	return getNewCache(duration.toMinutes(), TimeUnit.MINUTES);
  }

  public static <K, V> @NotNull Cache<K, V> getNewCache() {
	return getNewCache(DEFAULT_CACHE_DURATION, DEFAULT_CACHE_TIME_UNIT);
  }
}
