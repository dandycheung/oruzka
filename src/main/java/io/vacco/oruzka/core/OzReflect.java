package io.vacco.oruzka.core;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class OzReflect {

  public static Class<?> toWrapperClass(Class<?> type) {
    if (!type.isPrimitive()) return type;
    else if (int.class.equals(type)) { return Integer.class; }
    else if (double.class.equals(type)) { return Double.class; }
    else if (char.class.equals(type)) { return Character.class; }
    else if (boolean.class.equals(type)) { return Boolean.class; }
    else if (long.class.equals(type)) { return Long.class; }
    else if (float.class.equals(type)) { return Float.class; }
    else if (short.class.equals(type)) { return Short.class; }
    else if (byte.class.equals(type)) { return Byte.class; }
    return type;
  }

  public static boolean isWrapperType(Class<?> type) {
    return type == Boolean.class
        || type == Integer.class
        || type == Character.class
        || type == Byte.class
        || type == Short.class
        || type == Double.class
        || type == Long.class
        || type == Float.class;
  }

  public static boolean isPrimitiveOrWrapper(final Class<?> type) {
    if (type == null) { return false; }
    return type.isPrimitive() || isWrapperType(type);
  }

  public static boolean isInteger(Object o) {
    return o instanceof Integer
        || o instanceof Byte
        || o instanceof Short
        || o instanceof Long;
  }

  public static boolean isRational(Object o) {
    return o instanceof Double || o instanceof Float;
  }

  public static boolean isBoolean(Object o) {
    return o instanceof Boolean;
  }

  public static boolean isEnum(Object o) {
    return o instanceof Enum<?>;
  }

  public static boolean isTextual(Object o) {
    return o instanceof String
        || o instanceof Character;
  }

  public static boolean isCollection(Object o) {
    return o instanceof List<?>
        || o instanceof Map<?, ?>
        || o instanceof Set<?>
        || o != null && o.getClass().isArray();
  }

  public static boolean isBaseType(Object o) {
    return isInteger(o)
        || isRational(o)
        || isTextual(o)
        || isEnum(o)
        || isBoolean(o);
  }

}
