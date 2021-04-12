package io.vacco.oruzka.util;

import java.util.*;
import java.util.stream.*;

public class OzMaps {

  public static <K, V> Map.Entry<K, V> kv(K key, V value) {
    return new AbstractMap.SimpleEntry<>(key, value);
  }

  public static <K, V> Map<K, V> mapOn(Stream<Map.Entry<K, V>> entries) {
    return entries.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  @SafeVarargs
  public static <K, V> Map<K, V> mapOf(Map.Entry<K, V> ... entries) {
    return mapOn(Arrays.stream(entries));
  }

  @SafeVarargs
  public static <K, V> Map<K, V> concurrentMapOf(Map.Entry<K, V> ... entries) {
    return Arrays.stream(entries)
        .collect(Collectors.toConcurrentMap(Map.Entry::getKey, Map.Entry::getValue));
  }

}
