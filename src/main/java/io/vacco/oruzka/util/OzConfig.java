package io.vacco.oruzka.util;

import io.vacco.oruzka.core.OFnSupplier;
import io.vacco.oruzka.core.OzCheck;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

/**
 * Minimal class for loading configuration classes.
 * Configuration loading order is class path sources, then file/environment sources.
 * Data is serialized/deserialized with the functions of your choosing.
 */
public class OzConfig {

  public static <T> T load(Function<URL, Map<String, Object>> loadFn,
                           Function<Map<String, Object>, T> mapFn, URL... sources) {
    List<Map<String, Object>> srcVals = Arrays.stream(sources).map(loadFn).collect(Collectors.toList());
    Optional<Map<String, Object>> om = new OzPatchLeft().onMultiple(srcVals);
    OzCheck.isTrue(om.isPresent(), OzCheck.MISSING_DATA);
    Map<String, Object> merged = om.get();
    return mapFn.apply(merged);
  }

  public static <T> T loadFrom(Function<URL, Map<String, Object>> loadFn,
                               Function<Map<String, Object>, T> mapFn,
                               String[] classpathSources, String[] fileSources) {
    return load(loadFn, mapFn, Stream.concat(
        Arrays.stream(classpathSources).map(OzConfig.class::getResource),
        Arrays.stream(fileSources)
            .map(path -> new File(path).getAbsoluteFile())
            .map(f -> OFnSupplier.tryGet(() -> f.toURI().toURL()))
    ).toArray(URL[]::new));
  }

  public static <T> T loadEnv(Function<URL, Map<String, Object>> loadFn,
                            Function<Map<String, Object>, T> mapFn,
                            String envProperty, String ... classpathSources) {
    String[] envSources = System.getenv(envProperty).split(",");
    return loadFrom(loadFn, mapFn, classpathSources, envSources);
  }

}
