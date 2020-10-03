package io.vacco.oruzka.core;

/**
 * An unsafe supplier of type <code>T</code>.
 *
 * @param <T> a return type.
 */
@FunctionalInterface
public interface OFnSupplier<T> {

  /**
   * Run potentially unsafe code, with prior knowledge
   * that the former may throw any kind of exception.
   *
   * @return <code>T</code>, if the code ran correctly.
   * @throws Exception if any error occurred.
   */
  T get() throws Exception;

  static <T> T tryGet(OFnSupplier<T> sup) {
    try {
      return sup.get();
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}
