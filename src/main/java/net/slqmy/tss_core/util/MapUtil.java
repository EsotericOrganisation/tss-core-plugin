package net.slqmy.tss_core.util;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MapUtil {

  public static <K, V> @NotNull Map<V, K> invertMap(@NotNull Map<K, V> map) {
	Map<V, K> invertedMap = new HashMap<>();

	for (Map.Entry<K, V> entry : map.entrySet()) {
	  invertedMap.put(entry.getValue(), entry.getKey());
	}

	return invertedMap;
  }
}
