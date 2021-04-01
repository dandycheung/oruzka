package io.vacco.oruzka.core;

import java.util.*;
import java.util.function.Function;

import static java.util.Arrays.*;

public class OzArrays {

  private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

  public static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for (int j = 0; j < bytes.length; j++) {
      int v = bytes[j] & 0xFF;
      hexChars[j * 2] = HEX_ARRAY[v >>> 4];
      hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
    }
    return new String(hexChars);
  }

  public static byte[] concat(byte[] first, byte[] second) {
    byte[] result = copyOf(first, first.length + second.length);
    System.arraycopy(second, 0, result, first.length, second.length);
    return result;
  }

  public static Optional<byte[]> fnConcat(Function<Object, String> idFn, Object ... values) {
    return stream(values)
        .filter(Objects::nonNull)
        .map(idFn)
        .map(String::getBytes)
        .reduce(OzArrays::concat);
  }

  public static Optional<byte[]> toStringConcat(Object ... values) {
    return fnConcat(Object::toString, values);
  }
}
