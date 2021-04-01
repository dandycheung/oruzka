package io.vacco.oruzka.io;

import io.vacco.oruzka.core.OFnSupplier;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import java.util.stream.*;

public class OzIo {

  public static <T> T withUtf8Lines(URL u, Function<Stream<String>, T> fn) throws IOException {
    T out;
    try (InputStream in = u.openStream()) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
      out = fn.apply(reader.lines());
    }
    return out;
  }

  public static String loadFrom(URL u) {
    return OFnSupplier.tryGet(() -> withUtf8Lines(
        u, str -> str.collect(Collectors.joining(System.lineSeparator()))
    ));
  }

  public static String[] loadLinesFrom(URL u) {
    return OFnSupplier.tryGet(() -> withUtf8Lines(
        u, str -> str.toArray(String[]::new)
    ));
  }

}
